package com.wei.mobileoffice.ui.pulltorefresh;


import com.wei.mobileoffice.platform.util.Logger;

public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
        Logger.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

}
