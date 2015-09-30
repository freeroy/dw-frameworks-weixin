package org.developerworld.frameworks.weixin.message.request;

/**
 * 群发信息响应事件
 * @author Roy Huang
 * @version 20140428
 *
 */
public class MassEventRequestMessage extends EventRequestMessage {

	private long msgId;
	private String status;
	private long totalCount;
	private long filterCount;
	private long sentCount;
	private long errorCount;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getFilterCount() {
		return filterCount;
	}

	public void setFilterCount(long filterCount) {
		this.filterCount = filterCount;
	}

	public long getSentCount() {
		return sentCount;
	}

	public void setSentCount(long sentCount) {
		this.sentCount = sentCount;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	@Override
	public String toString() {
		return "MassEventRequestMessage [msgId=" + msgId + ", status=" + status
				+ ", totalCount=" + totalCount + ", filterCount=" + filterCount
				+ ", sentCount=" + sentCount + ", errorCount=" + errorCount
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (errorCount ^ (errorCount >>> 32));
		result = prime * result + (int) (filterCount ^ (filterCount >>> 32));
		result = prime * result + (int) (msgId ^ (msgId >>> 32));
		result = prime * result + (int) (sentCount ^ (sentCount >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (int) (totalCount ^ (totalCount >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MassEventRequestMessage other = (MassEventRequestMessage) obj;
		if (errorCount != other.errorCount)
			return false;
		if (filterCount != other.filterCount)
			return false;
		if (msgId != other.msgId)
			return false;
		if (sentCount != other.sentCount)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (totalCount != other.totalCount)
			return false;
		return true;
	}

}
