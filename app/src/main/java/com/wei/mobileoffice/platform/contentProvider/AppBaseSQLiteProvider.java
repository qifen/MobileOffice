package com.wei.mobileoffice.platform.contentProvider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.wei.mobileoffice.platform.util.Logger;
import com.wei.mobileoffice.platform.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class AppBaseSQLiteProvider extends ContentProvider {

    private static final String TAG = AppBaseSQLiteProvider.class.getSimpleName();
    public static final String CUSTOM_PREFIX = "[@";
    public static final String CUSTOM_SUFFIX = "]";
    public static final String LIMIT = "limit";
    public static final String LIMIT_PREFIX = CUSTOM_PREFIX + LIMIT;

    //获取本地数据库版本号
    public abstract int getDBVersion();

    //注册tables
    public abstract void registerTables(Map<Integer, IDBTable> mTables, UriMatcher sUriMatcher);

    //全局数据库
    public static final String GLOBAL_DB_NAME = "all_data.db";
    //用户数据库，每个登陆的用户都维护一个
    public static final String USER_DB_NAME = "user_data_{0}.db";

    private static boolean isRegisted = false;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //uriMatchCode与IDBTable的Map
    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, IDBTable> mTables = new HashMap<Integer, IDBTable>();

    private static final List<IDBTable> globalDBTableList = new ArrayList<IDBTable>();

    private static final List<IDBTable> userDBTableList = new ArrayList<IDBTable>();

    private Map<String, SQLiteOpenHelper> mOpenHelpers = new WeakHashMap<String, SQLiteOpenHelper>();

    public AppBaseSQLiteProvider(){

        if(isRegisted){
            return;
        }

        //注册table
        registerTables(mTables, sUriMatcher);
        //setting
        //mTables.putAll(new AppSettingContract(-1).register(sUriMatcher));

        //将所有注册的table抽象按公有数据表和私有数据表梳理一下
        Set<String> tableNameSet4Global = new HashSet<String>();
        Set<String> tableNameSet4User = new HashSet<String>();
        for(Entry<Integer, IDBTable> entry : mTables.entrySet()){
            IDBTable table = entry.getValue();
            String tableName = table.getTableName();
            if(table.isUserDB()){
                if(!tableNameSet4User.contains(tableName)){
                    tableNameSet4User.add(tableName);
                    userDBTableList.add(table);
                }
            }else{
                if(!tableNameSet4Global.contains(tableName)){
                    tableNameSet4Global.add(tableName);
                    globalDBTableList.add(table);
                }
            }
        }

        isRegisted = true;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uri != null) {
            IDBTable table = getTableImpl(uri);
            SQLiteOpenHelper mDBHelper = initDataBase(table.getDBName());
            SQLiteQueryBuilder qb = table.query(uri);
            if(qb==null){
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();

            /**
             * selection扩展，如下
             * _id = ? ... [@limit 2,10]
             * [@limit xxx] 解析为limit值
             */
            String limit = null;
            if(selection!=null){
                int limitIndex = selection.indexOf(LIMIT_PREFIX);
                int endIndex = -1;
                if(limitIndex>=0){
                    endIndex = selection.indexOf(CUSTOM_SUFFIX, limitIndex);
                    if(endIndex<0){
                        //throw new RuntimeException
                    }else{
                        limit = selection.substring(limitIndex, endIndex + 1);
                        selection = selection.replace(limit, "");
                        limit = limit.substring(LIMIT_PREFIX.length(), limit.length()-CUSTOM_SUFFIX.length());
                    }
                }
            }

            Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder, limit);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        IDBTable table = mTables.get(sUriMatcher.match(uri));
        if(table==null){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table.getType();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("invalid contentvalues");
        }
        IDBTable table = getTableImpl(uri);
        SQLiteOpenHelper mDBHelper = initDataBase(table.getDBName());
        String tableName = table.getTableName();
        Uri contentUri = table.getCotentUri();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        boolean replace = false;
        if (values.containsKey(CommonBaseColumns.SQL_REPLACE)) {
            replace = values.getAsBoolean(CommonBaseColumns.SQL_REPLACE);
            values.remove(CommonBaseColumns.SQL_REPLACE);
        }
        long rowId = -1;
        if (replace) {
            rowId = db.replace(tableName, null, values);
        } else {
            rowId = db.insert(tableName, null, values);
        }
        if (rowId > 0) {
            Uri resultUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        IDBTable table = getTableImpl(uri);
        SQLiteOpenHelper mDBHelper = initDataBase(table.getDBName());

        String tableName = table.getTableName();
        boolean hasId = table.hasIdCode();
        int count;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (hasId) {
            String id = uri.getPathSegments().get(1);
            selection = BaseColumns._ID
                    + "="
                    + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                    + ')' : "");
        }
        count = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        IDBTable table = getTableImpl(uri);
        SQLiteOpenHelper mDBHelper = initDataBase(table.getDBName());

        String tableName = table.getTableName();
        boolean hasId = table.hasIdCode();

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (hasId) {
            String id = uri.getPathSegments().get(1);
            selection = BaseColumns._ID
                    + "="
                    + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                    + ')' : "");
        }
        int count = db.update(tableName, values, selection, selectionArgs);
        return count;
    }

    private IDBTable getTableImpl(Uri uri){
        IDBTable table = mTables.get(sUriMatcher.match(uri));
        if(table==null){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }

    private SQLiteOpenHelper initDataBase(String dbName) {
        SQLiteOpenHelper mDBHelper = null;
        if (StringUtil.isNotBlank(dbName)) {
            // 如果上次打开的文件跟本次不同且有未执行完任务，则锁定数据库
            Logger.d(TAG, "initDataBase:" + dbName);
            mDBHelper = mOpenHelpers.get(dbName);
            if(mDBHelper == null){
                mDBHelper = new DatabaseHelper(getContext(), dbName, null, getDBVersion());
                mOpenHelpers.put(dbName, mDBHelper);
            }
        } else {
            throw new IllegalArgumentException("dbName is blank");
        }
        return mDBHelper;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (values == null) {
            throw new IllegalArgumentException("invalid values");
        }
        if (values.length == 0) {
            return -1;
        }
        boolean replace = false;
        for (ContentValues value : values) {
            if (value.containsKey(CommonBaseColumns.SQL_REPLACE)) {
                replace = value.getAsBoolean(CommonBaseColumns.SQL_REPLACE);
                value.remove(CommonBaseColumns.SQL_REPLACE);
            }
        }

        IDBTable table = getTableImpl(uri);
        SQLiteOpenHelper mDBHelper = initDataBase(table.getDBName());
        String tableName = table.getTableName();
        Uri contentUri = table.getCotentUri();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();
        db.beginTransaction();
        int count = 0;
        for (ContentValues value : values) {
            long rowId = -1;
            if (replace) {
                rowId = db.replace(tableName, null, value);
            } else {
                rowId = db.insert(tableName, null, value);
            }
            if (rowId > 0) {
                Uri resultUri = ContentUris.withAppendedId(contentUri, rowId);
                resolver.notifyChange(resultUri, null);
                count++;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    @SuppressLint("Override")
    public void shutdown() {
        for (Entry<String, SQLiteOpenHelper> entry : mOpenHelpers.entrySet()) {
            SQLiteOpenHelper helper = entry.getValue();
            if(helper != null){
                helper.close();
            }
        }
        // super.shutdown();
    }

    private static final class DatabaseHelper extends SQLiteOpenHelper {

        private boolean mIsUserDb = true;

        private DatabaseHelper(Context context, String name,
                               CursorFactory factory, int version) {
            super(context, name, factory, version);
            if (TextUtils.equals(name, GLOBAL_DB_NAME)) {
                mIsUserDb = false;
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Logger.d(TAG, "oncreate");
            List<IDBTable> tmpDBTableList = null;
            if (mIsUserDb) {
                tmpDBTableList = userDBTableList;
            } else {
                tmpDBTableList = globalDBTableList;
            }
            for(IDBTable table : tmpDBTableList){
                db.execSQL(table.getTableDescSQL());
                createIndex(table.getIndexSQL(), db);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Logger.d(TAG, "onUpgrade");
            if (db == null) {
                return;
            }
            List<IDBTable> tmpDBTableList = null;
            List<String> lstColumns = null;
            if (mIsUserDb) {
                tmpDBTableList = userDBTableList;
            } else {
                tmpDBTableList = globalDBTableList;
            }
            for(IDBTable table : tmpDBTableList){
                lstColumns = compareResult(db, table.getTableName() , table.getTableDescSQL());
                alterTable(db, table.getTableName(), lstColumns);
            }

            onCreate(db);
        }

        @SuppressLint("Override")
        public void onDowngrade(SQLiteDatabase db, int oldVersion,
                                int newVersion) {
            Logger.d(TAG, "onDowngrade");
            onUpgrade(db, oldVersion, newVersion);
        }

        private void alterTable(SQLiteDatabase db, String TableName,
                                List<String> lstColumns) {
            if (db == null || TextUtils.isEmpty(TableName)) {
                return;
            }

            if (lstColumns != null && lstColumns.size() > 0) {
                for (String str : lstColumns) {
                    db.execSQL("ALTER TABLE " + TableName + " ADD " + str);
                }
            }
        }

        /**
         * zhaoxu修改 2012-3-28 从DataBaseManager从移过来，主要是避免每次DB初始化都进行字段的比较
         *
         * @param sqliteDatabase
         *            已经打开的DB
         * @param tableName
         *            数据库表名
         * @param createDesc
         *            创建表的SQL语句，主要用于提取字段名
         * @return 新增的字段列表，没有新增，则返回empty list，如果失败则null
         */
        private List<String> compareResult(SQLiteDatabase sqliteDatabase,
                                           String tableName, String createDesc) {
            if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(createDesc)) {
                return null;
            }

            if ((sqliteDatabase == null) || (!sqliteDatabase.isOpen())) {
                return null;
            }

            String desc = "select * from " + tableName + " where "
                    + BaseColumns._ID + "=0";
            Cursor cursor = null;

            try {
                cursor = sqliteDatabase.rawQuery(desc, null);
            } catch (SQLiteException e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            if (cursor != null) {
                String[] oldCols = cursor.getColumnNames();
                String[] newCols = createDesc.split(",");
                String temp = newCols[0];
                if (!TextUtils.isEmpty(temp)) {
                    int index = temp.indexOf("(");
                    if (index != -1 && (index + 1) < temp.length()) {
                        newCols[0] = temp.substring(index + 1);
                    }
                }
                temp = newCols[newCols.length - 1];
                if (!TextUtils.isEmpty(temp)) {
                    int index = temp.indexOf(")");
                    if (index != -1) {
                        newCols[newCols.length - 1] = temp.substring(0, index);
                    }
                }
                List<String> tempList = new ArrayList<String>();
                for (String newCol : newCols) {

                    String tempNewColName = newCol.trim();
                    if (newCol.contains("CONSTRAINT ")) {
                        break;
                    }
                    int nFirstSpace = tempNewColName.indexOf(' ');
                    if (nFirstSpace != -1) {
                        tempNewColName = tempNewColName.substring(0,
                                nFirstSpace);
                    }

                    boolean isMatch = false;
                    for (String oldCol : oldCols) {
                        oldCol = oldCol.trim();
                        if (tempNewColName.equals(oldCol)) {
                            isMatch = true;
                            break;
                        }
                    }
                    if (!isMatch) {
                        tempList.add(newCol);
                    }
                }
                cursor.close();
                return tempList;
            }

            return null;
        }

        private void createIndex(String[] indexes, SQLiteDatabase database) {
            if(indexes==null){
                return;
            }
            for (String str : indexes) {
                if (!TextUtils.isEmpty(str)) {
                    database.execSQL(str);
                }
            }
        }

    }

}
