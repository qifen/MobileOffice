package com.wei.mobileoffice.platform.util;

import android.util.Log;

import com.wei.mobileoffice.platform.config.AppConfigFactory;


/**
 * @author leixiao
 *
 */
public class Logger {

	public static int v(String tag, String msg) {
		if(canLog()){
			return Log.v(tag, msg);
		}
		return -1;
	}
	
	public static int v(String tag, String msg, Throwable tr) {
		if(canLog()){
			return Log.v(tag, msg, tr);
		}
		return -1;
	}
	
	public static int d(String tag, String msg) {
		if(canLog()){
			return Log.d(tag, msg);
		}
		return -1;
	}
	
	public static int d(String tag, String msg, Throwable tr) {
		if(canLog()){
			return Log.d(tag, msg, tr);
		}
		return -1;
	}
	
	public static int i(String tag, String msg) {
		if(canLog()){
			return Log.i(tag, msg);
		}
		return -1;
	}
	
	public static int i(String tag, String msg, Throwable tr) {
		if(canLog()){
			return Log.i(tag, msg, tr);
		}
		return -1;
	}
	
	public static int w(String tag, String msg) {
		if(canLog()){
			return Log.w(tag, msg);
		}
		return -1;
	}
	
	public static int w(String tag, String msg, Throwable tr) {
		if(canLog()){
			return Log.w(tag, msg, tr);
		}
		return -1;
	}
	
	public static int e(String tag, String msg) {
		if(canLog()){
			return Log.e(tag, msg);
		}
		return -1;
	}
	
	public static int e(String tag, String msg, Throwable tr) {
		if(canLog()){
			return Log.e(tag, msg, tr);
		}
		return -1;
	}

    private static boolean canLog(){
        if(AppConfigFactory.getAppConfig().isDev()){
            return true;
        }else{
            return false;
        }
    }
	
}
