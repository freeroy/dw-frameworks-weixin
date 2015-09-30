package org.developerworld.frameworks.weixin.api.dto;

/**
 * 微信红包
 * @author Roy Huang
 * @version 20150301
 *
 */
public class RedPack {

	private Integer totalAmount;
	private Integer minValue;
	private Integer MaxValue;
	private Integer totalNum;
	private String wishing;
	private String actName;
	private String remark;
	private String shareContent;
	private String shareUrl;
	private String shareImgurl;

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return MaxValue;
	}

	public void setMaxValue(Integer maxValue) {
		MaxValue = maxValue;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getWishing() {
		return wishing;
	}

	public void setWishing(String wishing) {
		this.wishing = wishing;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getShareImgurl() {
		return shareImgurl;
	}

	public void setShareImgurl(String shareImgurl) {
		this.shareImgurl = shareImgurl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((MaxValue == null) ? 0 : MaxValue.hashCode());
		result = prime * result + ((actName == null) ? 0 : actName.hashCode());
		result = prime * result
				+ ((minValue == null) ? 0 : minValue.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result
				+ ((shareContent == null) ? 0 : shareContent.hashCode());
		result = prime * result
				+ ((shareImgurl == null) ? 0 : shareImgurl.hashCode());
		result = prime * result
				+ ((shareUrl == null) ? 0 : shareUrl.hashCode());
		result = prime * result
				+ ((totalAmount == null) ? 0 : totalAmount.hashCode());
		result = prime * result
				+ ((totalNum == null) ? 0 : totalNum.hashCode());
		result = prime * result + ((wishing == null) ? 0 : wishing.hashCode());
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
		RedPack other = (RedPack) obj;
		if (MaxValue == null) {
			if (other.MaxValue != null)
				return false;
		} else if (!MaxValue.equals(other.MaxValue))
			return false;
		if (actName == null) {
			if (other.actName != null)
				return false;
		} else if (!actName.equals(other.actName))
			return false;
		if (minValue == null) {
			if (other.minValue != null)
				return false;
		} else if (!minValue.equals(other.minValue))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (shareContent == null) {
			if (other.shareContent != null)
				return false;
		} else if (!shareContent.equals(other.shareContent))
			return false;
		if (shareImgurl == null) {
			if (other.shareImgurl != null)
				return false;
		} else if (!shareImgurl.equals(other.shareImgurl))
			return false;
		if (shareUrl == null) {
			if (other.shareUrl != null)
				return false;
		} else if (!shareUrl.equals(other.shareUrl))
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		if (totalNum == null) {
			if (other.totalNum != null)
				return false;
		} else if (!totalNum.equals(other.totalNum))
			return false;
		if (wishing == null) {
			if (other.wishing != null)
				return false;
		} else if (!wishing.equals(other.wishing))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RedPack [totalAmount=" + totalAmount + ", minValue=" + minValue
				+ ", MaxValue=" + MaxValue + ", totalNum=" + totalNum
				+ ", wishing=" + wishing + ", actName=" + actName + ", remark="
				+ remark + ", shareContent=" + shareContent + ", shareUrl="
				+ shareUrl + ", shareImgurl=" + shareImgurl + "]";
	}

}
