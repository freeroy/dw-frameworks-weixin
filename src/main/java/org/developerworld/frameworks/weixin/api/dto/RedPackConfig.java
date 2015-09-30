package org.developerworld.frameworks.weixin.api.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 红包配置对象
 * 
 */
public class RedPackConfig {

	private byte[] apiClientCert;
	private String apiClientCertPassword;
	private String apiKey;
	private String mchId;
	private String subMchId;
	private String wxappid;
	private String nickName;
	private String sendName;
	private String logoImgurl;
	private String clientIp;

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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
		if (apiClientCertPassword == null)
			apiClientCertPassword = mchId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getWxappid() {
		return wxappid;
	}

	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getLogoImgurl() {
		return logoImgurl;
	}

	public void setLogoImgurl(String logoImgurl) {
		this.logoImgurl = logoImgurl;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		result = prime * result
				+ ((clientIp == null) ? 0 : clientIp.hashCode());
		result = prime * result
				+ ((logoImgurl == null) ? 0 : logoImgurl.hashCode());
		result = prime * result + ((mchId == null) ? 0 : mchId.hashCode());
		result = prime * result
				+ ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result
				+ ((sendName == null) ? 0 : sendName.hashCode());
		result = prime * result
				+ ((subMchId == null) ? 0 : subMchId.hashCode());
		result = prime * result + ((wxappid == null) ? 0 : wxappid.hashCode());
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
		RedPackConfig other = (RedPackConfig) obj;
		if (apiKey == null) {
			if (other.apiKey != null)
				return false;
		} else if (!apiKey.equals(other.apiKey))
			return false;
		if (clientIp == null) {
			if (other.clientIp != null)
				return false;
		} else if (!clientIp.equals(other.clientIp))
			return false;
		if (logoImgurl == null) {
			if (other.logoImgurl != null)
				return false;
		} else if (!logoImgurl.equals(other.logoImgurl))
			return false;
		if (mchId == null) {
			if (other.mchId != null)
				return false;
		} else if (!mchId.equals(other.mchId))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (sendName == null) {
			if (other.sendName != null)
				return false;
		} else if (!sendName.equals(other.sendName))
			return false;
		if (subMchId == null) {
			if (other.subMchId != null)
				return false;
		} else if (!subMchId.equals(other.subMchId))
			return false;
		if (wxappid == null) {
			if (other.wxappid != null)
				return false;
		} else if (!wxappid.equals(other.wxappid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RedPackConfig [apiKey=" + apiKey + ", mchId=" + mchId
				+ ", subMchId=" + subMchId + ", wxappid=" + wxappid
				+ ", nickName=" + nickName + ", sendName=" + sendName
				+ ", logoImgurl=" + logoImgurl + ", clientIp=" + clientIp + "]";
	}

}
