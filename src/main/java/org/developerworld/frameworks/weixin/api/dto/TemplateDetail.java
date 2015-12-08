package org.developerworld.frameworks.weixin.api.dto;

public class TemplateDetail {

	public TemplateDetail() {

	}

	public TemplateDetail(String value, String color) {
		this.value = value;
		this.color = color;
	}

	public TemplateDetail(String value) {
		this.value = value;
	}

	private String value;

	private String color = "#CCCCCC";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		TemplateDetail other = (TemplateDetail) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TemplateDetail [value=" + value + ", color=" + color + "]";
	}

}
