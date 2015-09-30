package org.developerworld.frameworks.weixin.api.dto;

/**
 * 媒体类型
 * @author Roy Huang
 * @version 20140312
 *
 */
public enum MediaType {

	IMAGE("image"), VOICE("voice"), VIDEO("video"), THUMB("thumb");

	private String name;

	private MediaType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}