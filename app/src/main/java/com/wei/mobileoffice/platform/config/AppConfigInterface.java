package com.wei.mobileoffice.platform.config;

import com.wei.mobileoffice.platform.constants.PlatformConstants.Stage;

/**
 *
 * 应用配置接口
 *
 */
public interface AppConfigInterface {

    /**
     * 获取appId
     */
    public String getAppId();

    /**
     * 获取渠道号
     */
    public String getChannelId();

    /**
     * 获取一次HTTP连接的最大请求时间
     * @return
     */
    public int getConnectionTimeout();

    /**
     * 获取socket连接的最大时间
     * @return
     */
    public int getSocketTimeout();

    /**
     * 是否开发模式
     * @return
     */
    public boolean isDev();

    /**
     * 获取当前应用的级别
     */
    public Stage getStage();
    /**
     * 获取当前app的使用者唯一ID，用于创建数据库
     * @return
     */
    public String getUserId();

    /**
     * 获取当前app请求的agentId
     * @return
     */
    public String getAgentId();

    /**
     * 获取开发模式指定的代理
     * @return
     */
    public String[] getProxyWhenDevMode();

}
