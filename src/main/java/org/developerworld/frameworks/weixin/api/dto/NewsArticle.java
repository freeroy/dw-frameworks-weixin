package org.developerworld.frameworks.weixin.api.dto;

import java.io.Serializable;

/**
 * 图文文章
 * @author Roy Huang
 * @version 20140428
 */
public class NewsArticle  implements Serializable{
	
	/**
	 * 显示封面
	 */
	public final static Byte SHOW_COVER_PIC_SHOW=1;
	/**
	 * 不显示封面
	 */
	public final static Byte SHOW_COVER_PIC_UNSHOW=0;

	private String thumbMediaId;
	private String author;
	private String title;
	private String contentSourceUrl;
	private String content;
	private String digest;
	//是否显示封面，1为显示，0为不显示
	private Byte showCoverPic;

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public Byte getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(Byte showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime
				* result
				+ ((contentSourceUrl == null) ? 0 : contentSourceUrl.hashCode());
		result = prime * result + ((digest == null) ? 0 : digest.hashCode());
		result = prime * result
				+ ((showCoverPic == null) ? 0 : showCoverPic.hashCode());
		result = prime * result
				+ ((thumbMediaId == null) ? 0 : thumbMediaId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		NewsArticle other = (NewsArticle) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (contentSourceUrl == null) {
			if (other.contentSourceUrl != null)
				return false;
		} else if (!contentSourceUrl.equals(other.contentSourceUrl))
			return false;
		if (digest == null) {
			if (other.digest != null)
				return false;
		} else if (!digest.equals(other.digest))
			return false;
		if (showCoverPic == null) {
			if (other.showCoverPic != null)
				return false;
		} else if (!showCoverPic.equals(other.showCoverPic))
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
		return "NewsArticle [thumbMediaId=" + thumbMediaId + ", author="
				+ author + ", title=" + title + ", contentSourceUrl="
				+ contentSourceUrl + ", content=" + content + ", digest="
				+ digest + ", showCoverPic=" + showCoverPic + "]";
	}

}
