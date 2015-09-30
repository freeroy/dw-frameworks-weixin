package org.developerworld.frameworks.weixin.api.dto;

/**
 * 授权范围
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public enum WebAuthScope {

	SNSAPI_BASE("snsapi_base"), SNSAPI_USERINFO("snsapi_userinfo");

	private String name;

	private WebAuthScope(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
