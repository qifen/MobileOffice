package com.wei.mobileoffice.platform.http;

import android.content.res.AssetManager;

import com.wei.mobileoffice.platform.AppContextBase;
import com.wei.mobileoffice.platform.util.Logger;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 验证alibaba.com or alibaba-inc.com
 * 证书自定义的检查逻辑
 * @author leixiao
 */
public class CustomX509TrustManager implements X509TrustManager {

    Certificate xiaofangcomVerisignCert = null;

    X509TrustManager m_defaultMgr = null;

    public CustomX509TrustManager()
            throws Exception {

        InputStream insInc = null;

        try {
            // 从Asset中读取Verisign证书
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            AssetManager am = AppContextBase.getApplication().getAssets();
            insInc = am.open("crmwap.cer");
            xiaofangcomVerisignCert = cerFactory.generateCertificate(insInc);
        } catch (Exception e) {
            e.printStackTrace();
            xiaofangcomVerisignCert = null;
            Logger.e("usertrack_data", "1,verisign.cer initData fail");
        } finally {
            if (null != insInc) {
                insInc.close();
            }
        }

        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            TrustManager[] tms = tmf.getTrustManagers();
            for (int i = 0; i < tms.length; i++) {
                if (tms[i] instanceof X509TrustManager) {
                    this.m_defaultMgr = (X509TrustManager) tms[i];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.m_defaultMgr = null;
            Logger.e("usertrack_data", "6,got default trust fail");
        }

    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        Logger.d("https", "checkClientTrusted");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        Logger.d("https", "https verify begin");

        Certificate validCert = null;

        if (chain.length > 0) {
            try {
                String name = chain[0].getSubjectX500Principal().getName();
                Logger.d("https", name);
                if (name.contains(".xiaofang.com")) {
                    validCert = xiaofangcomVerisignCert;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (StackOverflowError error) {
            }
        }

        boolean needCustomValid = true;
        if (null == validCert) {
            needCustomValid = false;
            //return;//证书为空，说明证书加载失败或者不需要验证的域名，降级到全信任模式
        }

        if(needCustomValid){
            for (int i = 0; i < chain.length; i++) {

                X509Certificate cert = chain[i];
                // 验证时间有效性
                try {
                    cert.checkValidity();
                    // test
                    // throw new CertificateExpiredException();
                } catch (CertificateExpiredException ce) {
                    // 证书过期
                    throw ce;
                } catch (CertificateNotYetValidException ce) {
                    // 证书还未生效
                    throw ce;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean exceptionOccur = false; // flag:验证过程中是否出现异常
                //验证公钥
                try {
                    byte[] pk = cert.getPublicKey().getEncoded();
                    byte[] cvpk = validCert.getPublicKey().getEncoded();
                    if(pk==null || cvpk==null || cvpk.length>pk.length){
                        exceptionOccur = true;
                    }
                    if(!exceptionOccur && cvpk!=null){
                        for(int j=0,length=cvpk.length;j<length;j++){
                            if(cvpk[j] != pk[j]){
                                exceptionOccur = true;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exceptionOccur = true;
                } catch (StackOverflowError error) {
                }

                if (!exceptionOccur) {
                    // pass one certificate in the chain, means success
                    Logger.d("https", "verify success");
                    return;
                }

            }

            // no chain passed
            Logger.d("https", " customize https verify failed");
        }

        // 调用系统的Default实现
        if (null != m_defaultMgr) {
            try {
                m_defaultMgr.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                // Default 捕获的CertificateException
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            } catch (StackOverflowError error) {
            }
        } else {
            // DefualtMgr不存在,降级为全信任
            return;

        }

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
