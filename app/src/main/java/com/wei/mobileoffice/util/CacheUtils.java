package com.wei.mobileoffice.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
	
	private static final String CACHE_FILE_NAME = "xinzhu";
	private static SharedPreferences mSharedPreferences;

	/**
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putString(key, value).commit();
	}
	
	/**
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getString(key, defValue);
	}

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        if(mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        if(mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, defValue);
    }
}
