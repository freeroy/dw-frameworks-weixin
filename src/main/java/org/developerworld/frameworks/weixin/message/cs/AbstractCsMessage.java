package org.developerworld.frameworks.weixin.message.cs;

import org.developerworld.frameworks.weixin.message.CsMessage;

/**
 * 抽象客服信息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public abstract class AbstractCsMessage implements CsMessage {

	private String toUser;

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((toUser == null) ? 0 : toUser.hashCode());
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
		AbstractCsMessage other = (AbstractCsMessage) obj;
		if (toUser == null) {
			if (other.toUser != null)
				return false;
		} else if (!toUser.equals(other.toUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractCsMessage [toUser=" + toUser + "]";
	}

}
