package com.wei.mobileoffice.constants;


import com.wei.mobileoffice.platform.constants.BaseReturnCode;

/**
 *
 * @author leixiao
 *
 */
public class ReturnCode extends BaseReturnCode {

    private ReturnCode(){}

    //插件操作
    public static final int PLUGIN_INSTALL_SUCCESS				  = 210;
    public static final int PLUGIN_UNINSTALL_SUCCESS			  = 212;
    public static final int PLUGIN_GETALL_SUCCESS			      = 213;
    public static final int PLUGIN_GETALL_NO_UPDATE    			  = 214;
    public static final int PLUGIN_NO_PERMISSION_INSTALL		  = 211100;
    public static final int PLUGIN_CRM_AUTH_INFO_LESS			  = 211101;
    public static final int PLUGIN_NOT_EXIST			  		  = 211102;
    public static final int PLUGIN_NOT_INSTALLED			  	  = 211103;
    public static final int PLUGIN_CRM_NO_PERMISSION_CURRENT_ROLE = 211104;
    public static final int PLUGIN_IS_NOT_AVAILABLE				  = 211105;
    public static final int PLUGIN_CUNTAO_AUTH_INFO_LESS		  = 211107;
    public static final int PLUGIN_CUNTAO_NO_PERMISSION_CURRENT	  = 211108;


}
