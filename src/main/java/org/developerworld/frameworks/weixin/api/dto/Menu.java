package org.developerworld.frameworks.weixin.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单数据
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class Menu  implements Serializable{

	private MenuType type;
	private String name;
	private String key;
	private String url;
	private List<Menu> subButton=new ArrayList<Menu>();

	public MenuType getType() {
		return type;
	}

	public void setType(MenuType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getSubButton() {
		return subButton;
	}

	public void setSubButton(List<Menu> subButton) {
		this.subButton = subButton;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((subButton == null) ? 0 : subButton.hashCode());
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
		Menu other = (Menu) obj;
		if (type != other.type)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (subButton == null) {
			if (other.subButton != null)
				return false;
		} else if (!subButton.equals(other.subButton))
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
		return "Menu [type=" + type + ", name=" + name + ", key="
				+ key + ", url=" + url + ", subButton=" + subButton + "]";
	}
}
