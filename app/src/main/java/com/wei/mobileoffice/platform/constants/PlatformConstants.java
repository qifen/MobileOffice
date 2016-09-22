package com.wei.mobileoffice.platform.constants;


/**
 *
 * 服务端交互协议
 * @author leixiao
 *
 */
public class PlatformConstants {
    /**
     * RQF_PLATEFORM_VERSION -> RQF_PLATFORM_VERSION
     */
    public class ReqProtocol {
        // 协议最外层的key
        public static final String RQ = "requestData";
        // 协议内部的字段
        public static final String RQF_APP_ID = "appID";// 应用ID
        public static final String RQF_OP_TYPE = "operationType";// 操作类型
        public static final String RQF_LANG = "lang";// 客户端语言，lang-country格式
        public static final String RQF_DEVICE_ID = "deviceId";// 客户端设备ID
        public static final String RQF_PLATFORM = "platform";// 客户端的平台
        public static final String RQF_PLATFORM_VERSION = "platformVersion";// 客户端平台的版本号
        public static final String RQF_CLIENT_VERSION = "clientVersion";// 客户端的版本号
        public static final String RQF_APP_CHANNEL_ID = "app_channelId";// 客户端的渠道号
        public static final String RQF_AUTHORIZATION = "Authorization";// 客户端的token
        public static final String RQF_USERNAME = "Username";// 客户端的登录名
        public static final String RQF_SIGN = "sign";// 请求参数的签名
        public static final String RQF_MULTI_INVOKERS = "_operations";//multiInvokeAction
    }

    public class RespProtocal {
        public static final String RPF_TYPE = "type";// 执行结果类型，一般的请求可以不使用
        public static final String RPF_STATUS = "resultcode";// 执行结果状态值
        public static final String RPF_MSG = "resultdesc";// 执行结果的备注，一般放错误提示
        public static final String RPF_OBJ = "obj";// 执行结果返回值

    }

    //上传文件参数前缀
    public static final String ATTACH_FIELD_PREFIX = "_file_";

    //标识jsonParse的方式
    public static final String H5_JSON_PARSE = "__h5JsonParse";

    // app的启动模式
    public enum Stage {
        DEVELOPMENT, TESTING, PRERELEASE, PRODUCTION
    };

    /**
     * 基础登录业务的常量
     */
    //获得公钥
    public static final String OP_GET_RSA_KEY 		= "getRSAPKey";
    public static final String RPF_RSA_PK		 	= "rsaPK";
    public static final String RPF_RSA_TS 			= "rsaTS";
    //登陆
    public static final String OP_LOGIN		  		= "login";
    public static final String RQF_LOGIN_ID 		= "loginId";
    public static final String RQF_LOGIN_PASSWORD 	= "loginPassword";
    public static final String OAUTH_USER_NAME		= "userName";
    public static final String OAUTH_ACCESS_TOKEN   = "accessToken";
    public static final String OAUTH_REFRESH_TOKEN  = "refreshToken";
    public static final String OAUTH_EXPIRED_ON     = "expiredOn";//client key
    public static final String OAUTH_ACCESSTOKEN_TIMEOUT = "accessTokenTimeout";//server key
    public static final String OAUTH_EXT_KEYS       = "oAuthExtKeys";
    public static final String OAUTH_EXT_PERFIX     = "oAuthExt_";

    //登出
    public static final String OP_LOGOUT		  	= "logout";
    //token校验
    public static final String OP_VALID_TOKEN		= "validToken";
    //刷新token
    public static final String OP_REFRESH_TOKEN     = "refreshToken";

}
