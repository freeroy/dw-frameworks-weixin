package org.developerworld.frameworks.weixin.api.dto;

public class CustomerService {

	/**
	 * 电脑在线
	 */
	public final static Byte STATUS_PC_ONLINE = 1;
	/**
	 * 手机在线
	 */
	public final static Byte STATUS_MOBILE_ONLINE = 2;
	/**
	 * 同时在线
	 */
	public final static Byte STATUS_ALL_ONLINE = 3;

	// 完整客服账号，格式为：账号前缀@公众号微信号
	private String kfAccount;
	// 客服工号
	private String kfId;
	// 客服昵称
	private String kfNick;
	// 客服在线状态 1：pc在线，2：手机在线。若pc和手机同时在线则为 1+2=3
	private Byte status;
	// 客服头像地址
	private String kfHeadimg;
	// 客服设置的最大自动接入数
	private Integer autoAccept;
	// 客服当前正在接待的会话数
	private Integer acceptedCase;

	public String getKfAccount() {
		return kfAccount;
	}

	public void setKfAccount(String kfAccount) {
		this.kfAccount = kfAccount;
	}

	public String getKfId() {
		return kfId;
	}

	public void setKfId(String kfId) {
		this.kfId = kfId;
	}

	public String getKfNick() {
		return kfNick;
	}

	public void setKfNick(String kfNick) {
		this.kfNick = kfNick;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getKfHeadimg() {
		return kfHeadimg;
	}

	public void setKfHeadimg(String kfHeadimg) {
		this.kfHeadimg = kfHeadimg;
	}

	public Integer getAutoAccept() {
		return autoAccept;
	}

	public void setAutoAccept(Integer autoAccept) {
		this.autoAccept = autoAccept;
	}

	public Integer getAcceptedCase() {
		return acceptedCase;
	}

	public void setAcceptedCase(Integer acceptedCase) {
		this.acceptedCase = acceptedCase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acceptedCase == null) ? 0 : acceptedCase.hashCode());
		result = prime * result
				+ ((autoAccept == null) ? 0 : autoAccept.hashCode());
		result = prime * result
				+ ((kfAccount == null) ? 0 : kfAccount.hashCode());
		result = prime * result
				+ ((kfHeadimg == null) ? 0 : kfHeadimg.hashCode());
		result = prime * result + ((kfId == null) ? 0 : kfId.hashCode());
		result = prime * result + ((kfNick == null) ? 0 : kfNick.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		CustomerService other = (CustomerService) obj;
		if (acceptedCase == null) {
			if (other.acceptedCase != null)
				return false;
		} else if (!acceptedCase.equals(other.acceptedCase))
			return false;
		if (autoAccept == null) {
			if (other.autoAccept != null)
				return false;
		} else if (!autoAccept.equals(other.autoAccept))
			return false;
		if (kfAccount == null) {
			if (other.kfAccount != null)
				return false;
		} else if (!kfAccount.equals(other.kfAccount))
			return false;
		if (kfHeadimg == null) {
			if (other.kfHeadimg != null)
				return false;
		} else if (!kfHeadimg.equals(other.kfHeadimg))
			return false;
		if (kfId == null) {
			if (other.kfId != null)
				return false;
		} else if (!kfId.equals(other.kfId))
			return false;
		if (kfNick == null) {
			if (other.kfNick != null)
				return false;
		} else if (!kfNick.equals(other.kfNick))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomerService [kfAccount=" + kfAccount + ", kfId=" + kfId
				+ ", kfNick=" + kfNick + ", status=" + status + ", kfHeadimg="
				+ kfHeadimg + ", autoAccept=" + autoAccept + ", acceptedCase="
				+ acceptedCase + "]";
	}

}
