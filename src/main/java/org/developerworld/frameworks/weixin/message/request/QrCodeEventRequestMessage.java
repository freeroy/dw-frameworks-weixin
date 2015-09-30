package org.developerworld.frameworks.weixin.message.request;

/**
 * 二维码事件
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class QrCodeEventRequestMessage extends EventRequestMessage {

	private String eventKey;
	private String ticket;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((eventKey == null) ? 0 : eventKey.hashCode());
		result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
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
		QrCodeEventRequestMessage other = (QrCodeEventRequestMessage) obj;
		if (eventKey == null) {
			if (other.eventKey != null)
				return false;
		} else if (!eventKey.equals(other.eventKey))
			return false;
		if (ticket == null) {
			if (other.ticket != null)
				return false;
		} else if (!ticket.equals(other.ticket))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QRCodeEventRequestMessage [eventKey=" + eventKey + ", ticket="
				+ ticket + "]";
	}

}
