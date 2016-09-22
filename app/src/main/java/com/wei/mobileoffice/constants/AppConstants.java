package com.wei.mobileoffice.constants;

import com.wei.mobileoffice.OfficeAppContext;

import java.io.File;

public class AppConstants {

    public static final String INTENT_ORIGIN_ACTIVITY = "originActivity";
    private static final String PACKAGE_NAME = OfficeAppContext.getApplication().getPackageName();

    public static class FileConstants {
        public static final String APP_DIR_NAME = File.separator + PACKAGE_NAME;
        public static final String IMG_TOTAL_DIR = File.separator + "images";
        public static final String AVATAR_SUB_DIR = IMG_TOTAL_DIR + File.separator +  "avatar";
        public static final String IMG_SUB_DIR = IMG_TOTAL_DIR + File.separator + "img";
        public static final String PUB_IMG_DIR = File.separator + "photoAlbum";//相册，保存用户导出的照片
        public static final String UPDATE_DIR = File.separator + "update";// 升级目录
    }

    public static final String SERVICE_URL = "http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/";
    public static final String APPID = PACKAGE_NAME;

    // operationTpye相关信息
    public static final String OP_UPDATE            = "update";

    // 客户端相关信息
    public static final String OS_TYPE = "ANDROID";
    public static final String MOB_NUMBER = "Number";// 手机号
    public static final String MOB_RESOLUTION = "resolution";// 手机分辨率
    public static final String MOB_OS_VERSION = "osVersion";// 手机操作系统和版本
    public static final String APP_ID = "appID";// 应用ID
    public static final String APP_NAME = "appName";// 应用名
    public static final String APP_VERSION = "appVersion";// 应用版本
    public static final String NET_TYPE = "netType";// 网络类型
    public static final String MOB_TYPE = "mobType";// 手机型号
    public static final String MOB_BRAND = "mobBrand";// 手机厂商
    public static final String DEVICE_ID = "deviceId";// 客户端设备ID

    //plugin
    public static final String OP_PLUGIN_OPERATION = "pluginOperate";
    public static final String OP_PLUGIN_GETMINE = "pluginGetMine";
    public static final String OP_PLUGIN_GETALL = "getAllPlugins";
    public static final String OP_GET_HOME_PLUGINS = "getHomePlugins";
    public static final String RQF_PLUGIN_OP_TYPE = "pluginOpType";
    public static final String RQF_PLUGIN_ID = "pluginId";
    public static final String RQF_WIDGET_ID = "widgetId";
    public static final String RQF_PLUGIN_VERSION ="pluginVersion";

    // 更新检测返回
    public static final String RPF_UPDATE_NEED = "isNeedUpdate";
    public static final String RPF_UPDATE_URL = "url";
    public static final String RPF_PATCH_URL = "patchUrl";
    public static final String RPF_CHANNEL_UPDATE_URL = "updateChannelUrl";
    public static final String RPF_UPDATE_HINT = "updateHint";
    public static final String RPF_UPDATE_VERSION_NAME = "updateVersionName";
    public static final String RPF_FILE_SIZE = "fileSize";
    public static final String RPF_PATCH_SIZE = "patchSize";
    public static final String RPF_APK_MD5 = "fileMd5";
    public static final String RPF_FORCE_UPDATE = "forceUpdate";

    // HandlerMessageIDs相关信息
    public static final int REQUEST_RSA_ACTION = 0;// 获取RSA公钥
    public static final int REQUEST_LOGIN_ACTION = 1;// 登录
    public static final int REQUEST_UPDATE = 3;// 更新
    public static final int REQUEST_GET_BANNER_INFO = 7;// 更新
    public static final int REQUEST_GET_NOTIFY = 4;// 未读消息
    public static final int REQUEST_GET_MSG = 5;// 读取消息列表
    public static final int REQUEST_TOKEN_VALID = 6;//token验证
    public static final int REQUEST_GET_PUSH_SETTINGS = 8;
    public static final int REQUEST_OP_UPDATE_PUSH_SETTINGS = 9;
    public static final int REQUEST_PLUGIN_CHECK_CRM_PERMISSION = 50;// 插件的权限检测
    public static final int REQUEST_GET_PERMISSION = 55;//用户权限获取

}
