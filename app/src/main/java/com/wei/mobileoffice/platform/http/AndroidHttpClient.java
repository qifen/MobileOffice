package com.wei.mobileoffice.platform.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author leixiao
 *
 */
public final class AndroidHttpClient implements HttpClient
{

    /**
     * 创建一个自定义的HttpClient
     * @param userAgent
     * @return
     */
    public static AndroidHttpClient newInstance(String userAgent)
    {
        HttpParams params = new BasicHttpParams();

        //发送请求前检查连接可用性
        HttpConnectionParams.setStaleCheckingEnabled(params, true);

        //超时设置
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);

        //缓冲区设置
        HttpConnectionParams.setSocketBufferSize(params, 2 * 8192);

        //不允许处理redirect
        HttpClientParams.setRedirecting(params, false);
        HttpClientParams.setAuthenticating(params, false);

        //使用http1.1协议
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        //默认字符集设置
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        //设置usergAgent
        if (userAgent != null)
        {
            HttpProtocolParams.setUserAgent(params, userAgent);
        }

        //连接池超时配置
        ConnManagerParams.setTimeout(params, 5 * 1000);
        //连接池连接数上限
        ConnManagerParams.setMaxTotalConnections(params, 20);

        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, getCustomSchemeRegistry());

        return new AndroidHttpClient(manager, params);
    }

    /**
     * 获取自定义的schemeRegistry
     * 解决ssl校验问题
     * @return
     */
    private static SchemeRegistry getCustomSchemeRegistry(){
        KeyStore trustStore = null;
        SSLSocketFactory sslsf = null;

        try{
            trustStore= KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sslsf = new SSLSocketFactoryEx(trustStore);
        }catch(Exception ex){
            sslsf = SSLSocketFactory.getSocketFactory();
        }

        sslsf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

        //TODO port dynamic
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", sslsf, 443));
        return schemeRegistry;
    }

    private final DefaultHttpClient delegate;

    private AndroidHttpClient(ClientConnectionManager ccm, HttpParams params) {
        this.delegate = new DelegateHttpClient(ccm, params);
    }

    /**
     * Release resources associated with this client. You must call this, or
     * significant resources (sockets and memory) may be leaked.
     */
    public void close()
    {
        getConnectionManager().shutdown();
    }

    public HttpParams getParams()
    {
        return delegate.getParams();
    }

    public ClientConnectionManager getConnectionManager()
    {
        return delegate.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest request) throws IOException
    {
        return delegate.execute(request);
    }

    public HttpResponse execute(HttpUriRequest request, HttpContext context)
            throws IOException
    {
        return delegate.execute(request, context);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request)
            throws IOException, ClientProtocolException
    {
        HttpResponse httpResponse = delegate.execute(target, request);
        return httpResponse;
    }

    public HttpResponse execute(HttpHost target, HttpRequest request,
                                HttpContext context) throws IOException
    {
        return delegate.execute(target, request, context);
    }

    public <T> T execute(HttpUriRequest request,
                         ResponseHandler<? extends T> responseHandler) throws IOException
    {
        return delegate.execute(request, responseHandler);
    }

    public <T> T execute(HttpUriRequest request,
                         ResponseHandler<? extends T> responseHandler, HttpContext context)
            throws IOException
    {
        return delegate.execute(request, responseHandler, context);
    }

    public <T> T execute(HttpHost target, HttpRequest request,
                         ResponseHandler<? extends T> responseHandler) throws IOException
    {
        return delegate.execute(target, request, responseHandler);
    }

    public <T> T execute(HttpHost target, HttpRequest request,
                         ResponseHandler<? extends T> responseHandler, HttpContext context)
            throws IOException
    {
        return delegate.execute(target, request, responseHandler, context);
    }

    private static class DelegateHttpClient extends DefaultHttpClient
    {

        private DelegateHttpClient(ClientConnectionManager ccm,
                                   HttpParams params) {
            super(ccm, params);
            setHttpRequestRetryHandler(new HttpRetryHandler());
        }

        @Override
        protected HttpContext createHttpContext()
        {
            // Same as DefaultHttpClient.createHttpContext() minus the
            // cookie store.
            HttpContext context = new BasicHttpContext();
            context.setAttribute(ClientContext.AUTHSCHEME_REGISTRY,
                    getAuthSchemes());
            context.setAttribute(ClientContext.COOKIESPEC_REGISTRY,
                    getCookieSpecs());
            context.setAttribute(ClientContext.CREDS_PROVIDER,
                    getCredentialsProvider());
            return context;
        }
    }
}