package com.wei.mobileoffice.platform.http;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

/**
 * 自定义的重试处理器
 * @author leixiao
 *
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {

    private static final int RETRY_COUNT = 3;

    @Override
    public boolean retryRequest(final IOException exception, int executionCount,final HttpContext context) {
        if (exception == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        if (executionCount > RETRY_COUNT) {
            //超过RETRY_COUNT次，不再重试
            return false;
        }
        if (exception instanceof NoHttpResponseException) {
            // Retry if the server dropped connection on us
            return true;
        }
        if (exception instanceof InterruptedIOException) {
            // Timeout
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            return false;
        }
        if (exception instanceof SSLHandshakeException) {
            // SSL handshake exception
            return false;
        }

        // otherwise do not retry
        return false;
    }

}
