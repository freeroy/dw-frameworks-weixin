package org.developerworld.frameworks.weixin.api.dto;

public class PayJsSign {

	private String appId;
	private Long timeStamp;
	private String nonceStr;
	private String _package;
	private String signType;
	private String paySign;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPackage() {
		return _package;
	}

	public void setPackage(String _package) {
		this._package = _package;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	@Override
	public String toString() {
		return "PayJsSign [appId=" + appId + ", timeStamp=" + timeStamp
				+ ", nonceStr=" + nonceStr + ", package=" + _package
				+ ", signType=" + signType + ", paySign=" + paySign + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_package == null) ? 0 : _package.hashCode());
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result
				+ ((nonceStr == null) ? 0 : nonceStr.hashCode());
		result = prime * result + ((paySign == null) ? 0 : paySign.hashCode());
		result = prime * result
				+ ((signType == null) ? 0 : signType.hashCode());
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		PayJsSign other = (PayJsSign) obj;
		if (_package == null) {
			if (other._package != null)
				return false;
		} else if (!_package.equals(other._package))
			return false;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		if (nonceStr == null) {
			if (other.nonceStr != null)
				return false;
		} else if (!nonceStr.equals(other.nonceStr))
			return false;
		if (paySign == null) {
			if (other.paySign != null)
				return false;
		} else if (!paySign.equals(other.paySign))
			return false;
		if (signType == null) {
			if (other.signType != null)
				return false;
		} else if (!signType.equals(other.signType))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

}
