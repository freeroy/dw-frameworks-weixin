package org.developerworld.frameworks.weixin.message.request;

/**
 * 语言信息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class VoiceRequestMessage extends AbstractRequestMessage {

	private String mediaId;
	private String format;
	private String recongnition;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecongnition() {
		return recongnition;
	}

	public void setRecongnition(String recongnition) {
		this.recongnition = recongnition;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
		result = prime * result
				+ ((recongnition == null) ? 0 : recongnition.hashCode());
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
		VoiceRequestMessage other = (VoiceRequestMessage) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		if (recongnition == null) {
			if (other.recongnition != null)
				return false;
		} else if (!recongnition.equals(other.recongnition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VoiceRequestMessage [mediaId=" + mediaId + ", format=" + format
				+ ", recongnition=" + recongnition + "]";
	}
}
