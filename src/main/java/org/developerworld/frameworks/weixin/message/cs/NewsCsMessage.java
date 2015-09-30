package org.developerworld.frameworks.weixin.message.cs;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class NewsCsMessage extends AbstractCsMessage {

	private List<Article> atricles = new ArrayList<Article>();

	public List<Article> getAtricles() {
		return atricles;
	}

	public void setAtricles(List<Article> atricles) {
		this.atricles = atricles;
	}

	@Override
	public String toString() {
		return "NewsCsMessage [atricles=" + atricles + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((atricles == null) ? 0 : atricles.hashCode());
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
		NewsCsMessage other = (NewsCsMessage) obj;
		if (atricles == null) {
			if (other.atricles != null)
				return false;
		} else if (!atricles.equals(other.atricles))
			return false;
		return true;
	}

	public static class Article {

		private String title;
		private String description;
		private String picUrl;
		private String url;

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

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((description == null) ? 0 : description.hashCode());
			result = prime * result
					+ ((picUrl == null) ? 0 : picUrl.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			result = prime * result + ((url == null) ? 0 : url.hashCode());
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
			Article other = (Article) obj;
			if (description == null) {
				if (other.description != null)
					return false;
			} else if (!description.equals(other.description))
				return false;
			if (picUrl == null) {
				if (other.picUrl != null)
					return false;
			} else if (!picUrl.equals(other.picUrl))
				return false;
			if (title == null) {
				if (other.title != null)
					return false;
			} else if (!title.equals(other.title))
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Article [title=" + title + ", description=" + description
					+ ", picUrl=" + picUrl + ", url=" + url + "]";
		}

	}
}
