package com.wei.mobileoffice.platform.contentProvider;

import android.net.Uri;

public interface BaseProviderContract {
	
	/**
	 * the authority for the provider
	 */
	String AUTHORITY = "com.xinzhu.train.provider";

	/**
	 * A content:// style uri to the authority for the wx provider
	 */
	Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

}
