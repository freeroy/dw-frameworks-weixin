package org.developerworld.frameworks.weixin.api.dto;

public class PayUnifiedOrder {

	public final static String FEE_TYPE_CNY = "CNY";
	public final static String TRADE_TYPE_JSAPI = "JSAPI";
	public final static String TRADE_TYPE_NATIVE = "NATIVE";
	public final static String TRADE_TYPE_APP = "APP";
	public final static String TRADE_TYPE_WAP = "WAP";

	private String body;
	private String detail;
	private String attach;
	private String outTradeNo;
	private String feeType;
	private Integer totalFee;
	private String spbillCreateIp;
	private String timeStart;
	private String timeExpire;
	private String goodsTag;
	private String notifyUrl;
	private String tradeType;
	private String productId;
	private String openid;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getGoodsTag() {
		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return "PayUnifiedOrder [body=" + body + ", detail=" + detail
				+ ", attach=" + attach + ", outTradeNo=" + outTradeNo
				+ ", feeType=" + feeType + ", totalFee=" + totalFee
				+ ", spbillCreateIp=" + spbillCreateIp + ", timeStart="
				+ timeStart + ", timeExpire=" + timeExpire + ", goodsTag="
				+ goodsTag + ", notifyUrl=" + notifyUrl + ", tradeType="
				+ tradeType + ", productId=" + productId + ", openid=" + openid
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attach == null) ? 0 : attach.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + ((feeType == null) ? 0 : feeType.hashCode());
		result = prime * result
				+ ((goodsTag == null) ? 0 : goodsTag.hashCode());
		result = prime * result
				+ ((notifyUrl == null) ? 0 : notifyUrl.hashCode());
		result = prime * result + ((openid == null) ? 0 : openid.hashCode());
		result = prime * result
				+ ((outTradeNo == null) ? 0 : outTradeNo.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((spbillCreateIp == null) ? 0 : spbillCreateIp.hashCode());
		result = prime * result
				+ ((timeExpire == null) ? 0 : timeExpire.hashCode());
		result = prime * result
				+ ((timeStart == null) ? 0 : timeStart.hashCode());
		result = prime * result
				+ ((totalFee == null) ? 0 : totalFee.hashCode());
		result = prime * result
				+ ((tradeType == null) ? 0 : tradeType.hashCode());
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
		PayUnifiedOrder other = (PayUnifiedOrder) obj;
		if (attach == null) {
			if (other.attach != null)
				return false;
		} else if (!attach.equals(other.attach))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (feeType == null) {
			if (other.feeType != null)
				return false;
		} else if (!feeType.equals(other.feeType))
			return false;
		if (goodsTag == null) {
			if (other.goodsTag != null)
				return false;
		} else if (!goodsTag.equals(other.goodsTag))
			return false;
		if (notifyUrl == null) {
			if (other.notifyUrl != null)
				return false;
		} else if (!notifyUrl.equals(other.notifyUrl))
			return false;
		if (openid == null) {
			if (other.openid != null)
				return false;
		} else if (!openid.equals(other.openid))
			return false;
		if (outTradeNo == null) {
			if (other.outTradeNo != null)
				return false;
		} else if (!outTradeNo.equals(other.outTradeNo))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (spbillCreateIp == null) {
			if (other.spbillCreateIp != null)
				return false;
		} else if (!spbillCreateIp.equals(other.spbillCreateIp))
			return false;
		if (timeExpire == null) {
			if (other.timeExpire != null)
				return false;
		} else if (!timeExpire.equals(other.timeExpire))
			return false;
		if (timeStart == null) {
			if (other.timeStart != null)
				return false;
		} else if (!timeStart.equals(other.timeStart))
			return false;
		if (totalFee == null) {
			if (other.totalFee != null)
				return false;
		} else if (!totalFee.equals(other.totalFee))
			return false;
		if (tradeType == null) {
			if (other.tradeType != null)
				return false;
		} else if (!tradeType.equals(other.tradeType))
			return false;
		return true;
	}

}
