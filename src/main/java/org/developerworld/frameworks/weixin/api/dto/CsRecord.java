package org.developerworld.frameworks.weixin.api.dto;

public class CsRecord {

	/**
	 * 创建未接入会话
	 */
	public final static Integer OPERCODE_CREATE_JOIN_UP_SESSION = 1000;
	/**
	 * 接入会话
	 */
	public final static Integer OPERCODE_JOIN_UP_SESSION = 1001;
	/**
	 * 主动发起会话
	 */
	public final static Integer OPERCODE_START_JOIN_UP_SESSION = 1002;
	/**
	 * 关闭会话
	 */
	public final static Integer OPERCODE_CLOSE_SESSION = 1004;
	/**
	 * 抢接会话
	 */
	public final static Integer OPERCODE_FORESTALL_JOIN_UP_SESSION = 1005;
	/**
	 * 公众号收到消息
	 */
	public final static Integer OPERCODE_ACCOUNT_RECEIVE_MESSAGE = 2001;
	/**
	 * 客服发送消息
	 */
	public final static Integer OPERCODE_CS_SEND_MESSAGE = 2002;
	/**
	 * 客服收到消息
	 */
	public final static Integer OPERCODE_CS_RECEIVE_MESSAGE = 2003;

	// 客服账号
	private String worker;
	// 用户的标识，对当前公众号唯一
	private String openid;
	// 操作ID（会话状态）
	private Integer opercode;
	// 操作时间，UNIX时间戳
	private Long time;
	// 聊天记录
	private String text;

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getOpercode() {
		return opercode;
	}

	public void setOpercode(Integer opercode) {
		this.opercode = opercode;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((openid == null) ? 0 : openid.hashCode());
		result = prime * result
				+ ((opercode == null) ? 0 : opercode.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((worker == null) ? 0 : worker.hashCode());
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
		CsRecord other = (CsRecord) obj;
		if (openid == null) {
			if (other.openid != null)
				return false;
		} else if (!openid.equals(other.openid))
			return false;
		if (opercode == null) {
			if (other.opercode != null)
				return false;
		} else if (!opercode.equals(other.opercode))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (worker == null) {
			if (other.worker != null)
				return false;
		} else if (!worker.equals(other.worker))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CsRecord [worker=" + worker + ", openid=" + openid
				+ ", opercode=" + opercode + ", time=" + time + ", text="
				+ text + "]";
	}

}
