package com.wei.mobileoffice.constants;

import com.wei.mobileoffice.OfficeAppContext;

public class BroadcastAction {
    private static final String PACKAGE_NAME = OfficeAppContext.getApplication().getPackageName();

    //从登录界面手工登录成功
    public static final String ACTION_LOGIN = PACKAGE_NAME + ".intent.actin.ACTION_LOGIN";
    //登出
    public static final String ACTION_LOGOUT = PACKAGE_NAME + ".intent.actin.ACTION_LOGOUT";
    //从AppStart自动登录成功
    public static final String ACTION_AUTO_LOGIN = PACKAGE_NAME + ".intent.actin.ACTION_AUTO_LOGIN";
    //有新消息
    public static final String ACTION_NEW_MSG = PACKAGE_NAME + ".intent.action.ACTION_NEW_MSG";
    //更新消息提醒广播
    public static final String ACTION_UPDATE_MSG = PACKAGE_NAME + ".intent.action.ACTION_UPDATE_MSG";
    //软件升级
    public static final String ACTION_APP_CHECK_UPDATE = PACKAGE_NAME + ".intent.action.ACTION_APP_CHECK_UPDATE";
    //开始下载
    public static final String ACTION_START_DOWNLOAD = PACKAGE_NAME + ".intent.action.ACTION_START_DOWNLOAD";
    //网络变化需要刷新
    public static final String ACTION_NETWORKTYPE_CHANGED = PACKAGE_NAME + ".intent.action.ACTION_NETWORKTYPE_CHANGED";
    //收藏变化需要刷新
    public static final String ACTION_COLLECTION_CHANGED = PACKAGE_NAME + ".intent.action.ACTION_COLLECTION_CHANGED";
}
