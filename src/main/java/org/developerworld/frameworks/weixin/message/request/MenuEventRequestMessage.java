package org.developerworld.frameworks.weixin.message.request;

/**
 * 菜单点击事件
 * 
 * @author Roy Huang
 * @version 20140523
 * 
 */
public class MenuEventRequestMessage extends EventRequestMessage {
	private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((eventKey == null) ? 0 : eventKey.hashCode());
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
		MenuEventRequestMessage other = (MenuEventRequestMessage) obj;
		if (eventKey == null) {
			if (other.eventKey != null)
				return false;
		} else if (!eventKey.equals(other.eventKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MenuEventRequestMessage [eventKey=" + eventKey + "]";
	}
}
