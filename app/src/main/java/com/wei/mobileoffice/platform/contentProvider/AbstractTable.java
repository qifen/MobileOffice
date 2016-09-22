package com.wei.mobileoffice.platform.contentProvider;

import android.annotation.SuppressLint;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.wei.mobileoffice.platform.config.AppConfigFactory;
import com.wei.mobileoffice.platform.util.StringUtil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * IDBTable的一般抽象实现
 * @author leixiao
 *
 */
public abstract class AbstractTable implements IDBTable{

    protected int matchCode = -1;

    public AbstractTable(int matchCode){
        this.matchCode = matchCode;
    }

    @SuppressLint("UseSparseArrays")
    public Map<Integer, IDBTable> register(UriMatcher sUriMatcher){
        sUriMatcher.addURI(BaseProviderContract.AUTHORITY, getTableName(), getUriMatchCode());
        sUriMatcher.addURI(BaseProviderContract.AUTHORITY, getTableName() + "/#", getUriMatchIdCode());

        Map<Integer, IDBTable> retMap = new HashMap<Integer, IDBTable>();
        retMap.put(getUriMatchCode(), getInstance(getUriMatchCode()));
        retMap.put(getUriMatchIdCode(), getInstance(getUriMatchIdCode()));
        return retMap;
    }

    public abstract int getUriMatchCode();

    public abstract int getUriMatchIdCode();

    public abstract IDBTable getInstance(int matchCode);

    /**
     * 默认没有索引，有需要覆盖此方法
     */
    @Override
    public String[] getIndexSQL() {
        return null;
    }

    @Override
    public String getDBName() {
        if(isUserDB()){
            String userId = AppConfigFactory.getAppConfig().getUserId();
            return MessageFormat.format(AppBaseSQLiteProvider.USER_DB_NAME, StringUtil.isBlank(userId) ? "empty" : userId);
        }else{
            return AppBaseSQLiteProvider.GLOBAL_DB_NAME;
        }
    }

    @Override
    public boolean hasIdCode() {
        return (matchCode % 10) == 1;
    }

    /**
     * 默认实现
     */
    @Override
    public SQLiteQueryBuilder query(Uri uri) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        if(!hasIdCode()){
            qb.setTables(getTableName());
        }else{
            qb.setTables(getTableName());
            qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
        }
        return qb;
    }

    @Override
    public String getType() {
        if(!hasIdCode()){
            return getContentType();
        }else{
            return getContentItemType();
        }
    }

}
