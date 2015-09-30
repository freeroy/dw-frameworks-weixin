package org.developerworld.frameworks.weixin.message.mass;

import org.developerworld.frameworks.weixin.message.MassMessage;
import org.developerworld.frameworks.weixin.message.MsgType;

public class TextMassMessage implements MassMessage {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MsgType getMsgType() {
		return MsgType.TEXT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		TextMassMessage other = (TextMassMessage) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TextMassMessage [content=" + content + "]";
	}

}
