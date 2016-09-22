package com.wei.mobileoffice.platform.config;

import com.wei.mobileoffice.platform.constants.PlatformConstants.Stage;

/**
 * @author leixiao
 *
 */
public class StageChecker {

	public static boolean isDevModeCheck(Stage stage){
		if(stage==null){
			return false;
		}
		return Stage.DEVELOPMENT.equals(stage) || 
		   Stage.TESTING.equals(stage);
	}
	
}
