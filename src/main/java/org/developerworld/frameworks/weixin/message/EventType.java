package org.developerworld.frameworks.weixin.message;

/**
 * 时间类型
 * 
 * @author Roy Huang
 * @version 20140523
 * 
 */
public enum EventType {

	SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"), CLICK("CLICK"), VIEW(
			"VIEW"), SCAN("SCAN"), LOCATION("LOCATION"), MASSSENDJOBFINISH(
			"MASSSENDJOBFINISH");

	private String value;

	private EventType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
