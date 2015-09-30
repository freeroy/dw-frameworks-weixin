package org.developerworld.frameworks.weixin.api.dto;
/**
 * 菜单类型
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public enum MenuType {
	
	CLICK("click"), VIEW("view");

	private String name;

	private MenuType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
