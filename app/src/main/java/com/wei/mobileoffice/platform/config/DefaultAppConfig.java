package com.wei.mobileoffice.platform.config;

import com.wei.mobileoffice.platform.constants.PlatformConstants.Stage;

/**
 * @author leixiao
 *
 */

public class DefaultAppConfig implements AppConfigInterface{

    //socket连接超时时间
    public static final int SOCKET_TIMEOUT = 20 * 1000;
    public static final int SOCKET_TIMEOUT_DEV = 2000 * 1000;
    //http请求超时时间
    public static final int CONNECTION_TIMEOUT = 20 * 1000;
    public static final int CONNECTION_TIMEOUT_DEV = 2000 * 1000;

    @Override
    public int getConnectionTimeout() {
        if(getStage().equals(Stage.DEVELOPMENT)){
            return CONNECTION_TIMEOUT_DEV;
        }else{
            return CONNECTION_TIMEOUT;
        }
    }

    @Override
    public int getSocketTimeout() {
        if(getStage().equals(Stage.DEVELOPMENT)){
            return SOCKET_TIMEOUT_DEV;
        }else{
            return SOCKET_TIMEOUT;
        }
    }

    @Override
    public boolean isDev() {
        return StageChecker.isDevModeCheck(getStage());
    }

    @Override
    public Stage getStage() {
        return Stage.PRODUCTION;
    }

    private void throwUnRegistException(){
        throw new RuntimeException("you must register app's AppConfig");
    }

    @Override
    public String getAppId() {
        throwUnRegistException();
        return null;
    }

    @Override
    public String getUserId() {
        throwUnRegistException();
        return null;
    }

    @Override
    public String getChannelId() {
        return "";
    }

    @Override
    public String getAgentId() {
        return "appDefaultAgengId";
    }

    @Override
    public String[] getProxyWhenDevMode() {
        return null;
    }

}
