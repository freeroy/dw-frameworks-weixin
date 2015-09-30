package org.developerworld.frameworks.weixin.api.dto;

/**
 * 退款信息对象
 * @author Roy Huang
 * @version 20150505
 *
 */
public class PayRefund {

	public final static String REFUND_FEE_TYPE_CNY = "CNY";

	private String transactionId;
	private String outTradeNo;
	private String outRefundNo;
	private Integer totalFee;
	private Integer refundFee;
	private String refundFeeType;
	private String opUserId;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundFeeType() {
		return refundFeeType;
	}

	public void setRefundFeeType(String refundFeeType) {
		this.refundFeeType = refundFeeType;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((opUserId == null) ? 0 : opUserId.hashCode());
		result = prime * result
				+ ((outRefundNo == null) ? 0 : outRefundNo.hashCode());
		result = prime * result
				+ ((outTradeNo == null) ? 0 : outTradeNo.hashCode());
		result = prime * result
				+ ((refundFeeType == null) ? 0 : refundFeeType.hashCode());
		result = prime * result
				+ ((refundFee == null) ? 0 : refundFee.hashCode());
		result = prime * result
				+ ((totalFee == null) ? 0 : totalFee.hashCode());
		result = prime * result
				+ ((transactionId == null) ? 0 : transactionId.hashCode());
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
		PayRefund other = (PayRefund) obj;
		if (opUserId == null) {
			if (other.opUserId != null)
				return false;
		} else if (!opUserId.equals(other.opUserId))
			return false;
		if (outRefundNo == null) {
			if (other.outRefundNo != null)
				return false;
		} else if (!outRefundNo.equals(other.outRefundNo))
			return false;
		if (outTradeNo == null) {
			if (other.outTradeNo != null)
				return false;
		} else if (!outTradeNo.equals(other.outTradeNo))
			return false;
		if (refundFeeType == null) {
			if (other.refundFeeType != null)
				return false;
		} else if (!refundFeeType.equals(other.refundFeeType))
			return false;
		if (refundFee == null) {
			if (other.refundFee != null)
				return false;
		} else if (!refundFee.equals(other.refundFee))
			return false;
		if (totalFee == null) {
			if (other.totalFee != null)
				return false;
		} else if (!totalFee.equals(other.totalFee))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PayRefund [transactionId=" + transactionId + ", outTradeNo="
				+ outTradeNo + ", outRefundNo=" + outRefundNo + ", totalFee="
				+ totalFee + ", refundFee=" + refundFee + ", refundFeeType="
				+ refundFeeType + ", opUserId=" + opUserId + "]";
	}

}
