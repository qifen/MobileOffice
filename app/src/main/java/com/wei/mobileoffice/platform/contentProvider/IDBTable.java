package com.wei.mobileoffice.platform.contentProvider;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * 每一个数据库表的公共接口
 * @author leixiao
 *
 */
public interface IDBTable {

    /**
     * 获得表结构SQL
     * @return
     */
    public String getTableDescSQL();

    /**
     * 返回此表的索引SQL
     * @return
     */
    public String[] getIndexSQL();

    /**
     * 获得此表归属的数据库名称
     * @return
     */
    public String getDBName();

    /**
     * 获取表名
     * @return
     */
    public String getTableName();

    /**
     * 获取内容Uri
     * @return
     */
    public Uri getCotentUri();

    /**
     * 获取contentType
     * @return
     */
    public String getContentType();

    /**
     * 获取item的contentType
     * @return
     */
    public String getContentItemType();

    /**
     * 构造查询对象
     * @return
     */
    public SQLiteQueryBuilder query(Uri uri);

    /**
     * 返回mimeType
     * @param
     * @return
     */
    public String getType();

    /**
     * 匹配到的code是带ID的SQL操作
     * 约定code mod 10结果为0为不带ID，为1是带ID
     * @return
     */
    public boolean hasIdCode();

    /**
     * 是否单个用户独享数据库中的表
     * @return
     */
    public boolean isUserDB();

}
