package com.wei.mobileoffice.platform.constants;

/**
 * status >= 10000定义为异常
 * @author leixiao
 *
 */
public class BaseReturnCode {

    public static final int COMMON_SUCCESS						   = 1;//

    //更新检测成功标志
    public static final int UPDATE_SUCCESS						  = 2;//更新成功

    public static final int COMMON_BUSINESS_ERROR                  = 500;//业务方错误

    public static final int COMMON_ERROR						   = 10000;//异常code基准

    /**
     * 系统错误
     */
    public static final int UNKNOWN_ERROR                          = 40000; //未知错误
    public static final int EMPTY_PARAMTER_OR_SIGNATURE_ERROR      = 40001;
    public static final int EMPTY_PARAMTER                         = 40002;
    public static final int SYSTEM_CONFIG_ERROR                    = 40003; //系统配置错误

    /**
     * 业务相关错误
     */
    public static final int BUSINESS_ERROR                    = 49999;
    public static final int DATA_IS_NULL                      = 50000;
    public static final int ILLEGAL_ARGUMENTS                 = 49998;//非法参数

    //NetWorkManager Error
    public static final int ERROR_IO							   = 10001;
    public static final int ERROR_Protocol 						   = 10002;
    public static final int ERROR_REFRESH_TOKEN					   = 10003;//刷新token错误
    public static final int ERROR_RESP_NULL						   = 10004;


    //登陆标志
    public static final int AUTH_SUCCESS						  = 100;//登陆成功
    public static final int AUTH_FAILED						  	  = 100100;//登陆过程失败
    public static final int AUTH_TOKEN_EXPIRED					  = 100101;//token失效
    public static final int AUTH_CONFLICT					  	  = 100102;//被踢下线
    public static final int AUTH_LOGIN_FAIL_LIMIT				  = 100103;//连续5次登录失败,账号锁定半小时
    public static final int AUTH_LOGIN_PWD_ERROR				  = 100104;//用户名密码错误
    //public static final int AUTH_FAILED_NUL_MEMBER_SEQ		      = 100105;//获取memberSeq失败

}
