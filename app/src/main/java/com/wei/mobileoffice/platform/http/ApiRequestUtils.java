package com.wei.mobileoffice.platform.http;


import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.Logger;
import com.wei.mobileoffice.platform.util.StringUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author leixiao
 *
 */
public class ApiRequestUtils {

    private static final String TAG = "ApiRequestUtils";

    //如果同一个ApiRequestUtils实例有多个请求，只保留最后一个的引用
    //一般情况下建议一个ApiRequestUtils实例只发一个请求
    public HttpRequestBase lastRequestObject = null;

    public <T> T doPostRequest(String endpointUrl, List<NameValuePair> body,
                               ResponseHandler<T> responseHandler)
            throws IOException {
        HttpPost requestObject = new HttpPost(endpointUrl);
        lastRequestObject = requestObject;
        //param
        boolean hasAttach = false;
        try {
//            hasAttach = hasAttach(body);
        } catch (Exception ex) {
        }
        HttpEntity entity = null;
        if (!hasAttach) {
            entity = new UrlEncodedFormEntity(body, HTTP.UTF_8);
        } else {//文件上传
//            entity = makeAttachedEntity(body);
        }

        ((HttpPost) requestObject).setEntity(entity);
        return executeRequestWithRefresh(requestObject, responseHandler, false, hasAttach);
    }

    private <T> T executeRequestWithRefresh(HttpRequestBase request,
                                            ResponseHandler<T> responseHandler, boolean isRetry, boolean hasAttach)
            throws ClientProtocolException, IOException {
        Header authorizationHeader = null;
        // Authorization
        authorizationHeader = new BasicHeader("Authorization", "getAccessToken");
        request.addHeader(authorizationHeader);
        authorizationHeader = new BasicHeader("Username", "getUsername");
        request.addHeader(authorizationHeader);
        return executeRequest(request, responseHandler, hasAttach);
    }

    private <T> T executeRequest(HttpRequestBase request, ResponseHandler<T> responseHandler, boolean hasAttach)
            throws IOException {

        if (!hasAttach) {
            request.addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            request.addHeader("Accept-Encoding", "gzip");
            request.addHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        }

        AndroidHttpClient client = AndroidHttpClient.newInstance("AgentId");


        HttpParams httpParams = client.getParams();
        HttpHost proxy = getProxy();
        if (proxy != null) {
//            httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        try {
            return client.execute(request, responseHandler);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } catch (StackOverflowError e) {
            throw new IOException("StackOverflowError");
        } finally {
            client.close();
        }
    }

    public void cancleRequest() {
        if (lastRequestObject != null) {
            new Thread() {
                @Override
                public void run() {
                    lastRequestObject.abort();
                }
            }.start();
        }
    }

    public boolean isAborted() {
        if (lastRequestObject != null) {
            return lastRequestObject.isAborted();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static HttpHost getProxy() {
        HttpHost proxy = null;
        String proxyHost = android.net.Proxy.getDefaultHost();
        int port = android.net.Proxy.getDefaultPort();
        if ("isDev"=="isDev") {
            String[] proxyInfo = new String[]{"AppConfigFactory.getAppConfig().getProxyWhenDevMode()",""};
            if (proxyInfo != null) {
                String proxyHostTmp = proxyInfo[0];
                String proxyPortTmp = proxyInfo[1];
                if (StringUtil.isNotBlank(proxyHostTmp) && StringUtil.isNotBlank(proxyPortTmp)) {
                    proxyHost = proxyHostTmp;
                    try {
                        port = Integer.valueOf(proxyPortTmp);
                    } catch (NumberFormatException e) {
                        Logger.e(TAG, "proxy port is not number");
                    }
                }
            }
        }
        if (proxyHost != null)
            proxy = new HttpHost(proxyHost, port);
        return proxy;
    }

    private boolean hasAttach(List<NameValuePair> pairs) {
        for (NameValuePair item : pairs) {
            if (item.getName().equals(PlatformConstants.ReqProtocol.RQ)) {
                try {
                    String jsonStr = item.getValue();
                    JSONObject reqObj;
                    reqObj = new JSONObject(jsonStr);
                    for (Iterator<?> it = reqObj.keys(); it.hasNext(); ) {
                        String key = (String) it.next();
                        //String value = reqObj.optString(key);
                        if (key.indexOf(PlatformConstants.ATTACH_FIELD_PREFIX) > -1) {
                            return true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
