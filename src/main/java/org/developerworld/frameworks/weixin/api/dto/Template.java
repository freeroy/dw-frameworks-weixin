package org.developerworld.frameworks.weixin.api.dto;

import java.util.LinkedHashMap;

public class Template {

	private String touser;

	private String templateId;

	private String url;

	private String topcolor = "#FF0000";

	private LinkedHashMap<String, TemplateDetail> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public LinkedHashMap<String, TemplateDetail> getData() {
		return data;
	}

	public void setData(LinkedHashMap<String, TemplateDetail> data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((templateId == null) ? 0 : templateId.hashCode());
		result = prime * result
				+ ((topcolor == null) ? 0 : topcolor.hashCode());
		result = prime * result + ((touser == null) ? 0 : touser.hashCode());
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
		Template other = (Template) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (templateId == null) {
			if (other.templateId != null)
				return false;
		} else if (!templateId.equals(other.templateId))
			return false;
		if (topcolor == null) {
			if (other.topcolor != null)
				return false;
		} else if (!topcolor.equals(other.topcolor))
			return false;
		if (touser == null) {
			if (other.touser != null)
				return false;
		} else if (!touser.equals(other.touser))
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
		return "Template [touser=" + touser + ", templateId=" + templateId
				+ ", url=" + url + ", topcolor=" + topcolor + ", data=" + data
				+ "]";
	}

}
