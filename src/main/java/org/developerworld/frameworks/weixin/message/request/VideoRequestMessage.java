package org.developerworld.frameworks.weixin.message.request;

/**
 * 视频信息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class VideoRequestMessage extends AbstractRequestMessage {

	private String mediaId;
	private String thumbMediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "VideoRequestMessage [mediaId=" + mediaId + ", thumbMediaId="
				+ thumbMediaId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
		result = prime * result
				+ ((thumbMediaId == null) ? 0 : thumbMediaId.hashCode());
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
		VideoRequestMessage other = (VideoRequestMessage) obj;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		if (thumbMediaId == null) {
			if (other.thumbMediaId != null)
				return false;
		} else if (!thumbMediaId.equals(other.thumbMediaId))
			return false;
		return true;
	}

}
