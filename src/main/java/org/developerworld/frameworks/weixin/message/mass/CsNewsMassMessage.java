package org.developerworld.frameworks.weixin.message.mass;

import org.developerworld.frameworks.weixin.message.MassMessage;
import org.developerworld.frameworks.weixin.message.MsgType;
import org.developerworld.frameworks.weixin.message.cs.NewsCsMessage;

public class CsNewsMassMessage implements MassMessage {

	private NewsCsMessage newsCsMessage;

	public MsgType getMsgType() {
		return MsgType.NEWS;
	}

	public NewsCsMessage getNewsCsMessage() {
		return newsCsMessage;
	}

	public void setNewsCsMessage(NewsCsMessage newsCsMessage) {
		this.newsCsMessage = newsCsMessage;
	}

	@Override
	public String toString() {
		return "CsNewsMassMessage [newsCsMessage=" + newsCsMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newsCsMessage == null) ? 0 : newsCsMessage.hashCode());
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
		CsNewsMassMessage other = (CsNewsMassMessage) obj;
		if (newsCsMessage == null) {
			if (other.newsCsMessage != null)
				return false;
		} else if (!newsCsMessage.equals(other.newsCsMessage))
			return false;
		return true;
	}

}
