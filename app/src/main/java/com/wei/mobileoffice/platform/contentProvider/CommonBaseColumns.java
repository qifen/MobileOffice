package com.wei.mobileoffice.platform.contentProvider;

import android.provider.BaseColumns;

public interface CommonBaseColumns extends BaseColumns {
    /**
     * 去顶该项数据执行插入或者替换操作
     */
    public static final String SQL_REPLACE = "_sqlReplace";
}
