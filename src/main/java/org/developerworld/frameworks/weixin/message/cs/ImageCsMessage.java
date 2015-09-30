package org.developerworld.frameworks.weixin.message.cs;

/**
 * 图片消息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class ImageCsMessage extends AbstractCsMessage {

	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
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
		ImageCsMessage other = (ImageCsMessage) obj;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageCsMessage [mediaId=" + mediaId + "]";
	}

}
