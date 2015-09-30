package org.developerworld.frameworks.weixin.api.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

/**
 * 支付订单配置类
 * 
 * @author Roy Huang
 * @version 20150417
 * 
 */
public class PayOrderConfig {
	
	private byte[] apiClientCert;
	private String apiClientCertPassword;
	private String appid;
	private String mchId;
	private String deviceInfo;
	private String apiKey;
	
	public byte[] getApiClientCert() {
		return apiClientCert;
	}

	public void setApiClientCert(File apiClientCert) throws IOException {
		FileInputStream fis = new FileInputStream(apiClientCert);
		try {
			setApiClientCert(fis);
		} finally {
			fis.close();
		}
	}

	public void setApiClientCert(InputStream apiClientCert) throws IOException {
		setApiClientCert(IOUtils.toByteArray(apiClientCert));
	}

	public void setApiClientCert(byte[] apiClientCert) {
		this.apiClientCert = apiClientCert;
	}

	public String getApiClientCertPassword() {
		return apiClientCertPassword;
	}

	public void setApiClientCertPassword(String apiClientCertPassword) {
		this.apiClientCertPassword = apiClientCertPassword;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(apiClientCert);
		result = prime
				* result
				+ ((apiClientCertPassword == null) ? 0 : apiClientCertPassword
						.hashCode());
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		result = prime * result + ((appid == null) ? 0 : appid.hashCode());
		result = prime * result
				+ ((deviceInfo == null) ? 0 : deviceInfo.hashCode());
		result = prime * result + ((mchId == null) ? 0 : mchId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayOrderConfig other = (PayOrderConfig) obj;
		if (!Arrays.equals(apiClientCert, other.apiClientCert))
			return false;
		if (apiClientCertPassword == null) {
			if (other.apiClientCertPassword != null)
				return false;
		} else if (!apiClientCertPassword.equals(other.apiClientCertPassword))
			return false;
		if (apiKey == null) {
			if (other.apiKey != null)
				return false;
		} else if (!apiKey.equals(other.apiKey))
			return false;
		if (appid == null) {
			if (other.appid != null)
				return false;
		} else if (!appid.equals(other.appid))
			return false;
		if (deviceInfo == null) {
			if (other.deviceInfo != null)
				return false;
		} else if (!deviceInfo.equals(other.deviceInfo))
			return false;
		if (mchId == null) {
			if (other.mchId != null)
				return false;
		} else if (!mchId.equals(other.mchId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PayOrderConfig [apiClientCert="
				+ Arrays.toString(apiClientCert) + ", apiClientCertPassword="
				+ apiClientCertPassword + ", appid=" + appid + ", mchId="
				+ mchId + ", deviceInfo=" + deviceInfo + ", apiKey=" + apiKey
				+ "]";
	}

}
