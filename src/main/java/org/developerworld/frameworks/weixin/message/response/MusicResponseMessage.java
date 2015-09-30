package org.developerworld.frameworks.weixin.message.response;

/**
 * 音乐回复信息
 * @author Roy Huang
 * @version 20140307
 *
 */
public class MusicResponseMessage extends AbstractResponseMessage{

	private String title;
	private String description;
	private String musicUrl;
	private String hqMusicUrl;
	private String thumbMediaId;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((hqMusicUrl == null) ? 0 : hqMusicUrl.hashCode());
		result = prime * result
				+ ((musicUrl == null) ? 0 : musicUrl.hashCode());
		result = prime * result
				+ ((thumbMediaId == null) ? 0 : thumbMediaId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		MusicResponseMessage other = (MusicResponseMessage) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (hqMusicUrl == null) {
			if (other.hqMusicUrl != null)
				return false;
		} else if (!hqMusicUrl.equals(other.hqMusicUrl))
			return false;
		if (musicUrl == null) {
			if (other.musicUrl != null)
				return false;
		} else if (!musicUrl.equals(other.musicUrl))
			return false;
		if (thumbMediaId == null) {
			if (other.thumbMediaId != null)
				return false;
		} else if (!thumbMediaId.equals(other.thumbMediaId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MusicResponseMessage [title=" + title + ", description="
				+ description + ", musicUrl=" + musicUrl + ", hqMusicUrl="
				+ hqMusicUrl + ", thumbMediaId=" + thumbMediaId + "]";
	}
	
	
}
