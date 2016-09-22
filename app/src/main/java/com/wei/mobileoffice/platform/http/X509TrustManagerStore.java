package com.wei.mobileoffice.platform.http;

import javax.net.ssl.X509TrustManager;

/**
 * @author leixiao
 *
 */
public class X509TrustManagerStore {

	protected static X509TrustManager m_trustMgr = null;
	
	protected static X509TrustManager allTrustMgr = new X509TrustManager() {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain,
				String authType)
				throws java.security.cert.CertificateException {
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain,
				String authType)
				throws java.security.cert.CertificateException {

		}

	};

	public static X509TrustManager getX509TrustManager() {
		if (m_trustMgr == null) {
//			if(!AppConfigFactory.getAppConfig().isDev()){
				try {
					m_trustMgr = new CustomX509TrustManager();
				} catch (Exception e) {
					e.printStackTrace();
					m_trustMgr = allTrustMgr;
				}
//			}else{
//				m_trustMgr = allTrustMgr;
//			}
		}
		return m_trustMgr;
	}

}
