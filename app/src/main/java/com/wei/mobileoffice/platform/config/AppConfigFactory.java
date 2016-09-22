package com.wei.mobileoffice.platform.config;

/**
 * 
 * AppConfigFactory
 *
 */
public class AppConfigFactory {

	private static AppConfigInterface mRegisterAppConfig;
	private static AppConfigInterface defaultAppConfig = new DefaultAppConfig();
	
	public static AppConfigInterface getAppConfig(){
		if(mRegisterAppConfig==null){
			return defaultAppConfig; 
		}else{
			return mRegisterAppConfig;
		}
	}
	
	public static void registerAppConfig(AppConfigInterface registerAppConfig){
		mRegisterAppConfig = registerAppConfig;
	}
	
}
