package org.developerworld.frameworks.weixin.message.request;

import org.developerworld.frameworks.weixin.message.EventType;
import org.developerworld.frameworks.weixin.message.RequestMessage;

/**
 * 事件消息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class EventRequestMessage implements RequestMessage {

	private String toUserName;
	private String fromUserName;
	private EventType event;
	private long createTime;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public EventType getEvent() {
		return event;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (createTime ^ (createTime >>> 32));
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result
				+ ((fromUserName == null) ? 0 : fromUserName.hashCode());
		result = prime * result
				+ ((toUserName == null) ? 0 : toUserName.hashCode());
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
		EventRequestMessage other = (EventRequestMessage) obj;
		if (createTime != other.createTime)
			return false;
		if (event != other.event)
			return false;
		if (fromUserName == null) {
			if (other.fromUserName != null)
				return false;
		} else if (!fromUserName.equals(other.fromUserName))
			return false;
		if (toUserName == null) {
			if (other.toUserName != null)
				return false;
		} else if (!toUserName.equals(other.toUserName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventRequestMessage [toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", event=" + event
				+ ", createTime=" + createTime + "]";
	}

}
