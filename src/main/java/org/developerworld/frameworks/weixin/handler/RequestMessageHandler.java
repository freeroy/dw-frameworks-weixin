package org.developerworld.frameworks.weixin.handler;

import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;

/**
 * 请求信息处理器
 * @author Roy Huang
 * @version 20140310
 *
 */
public interface RequestMessageHandler {
	
	/**
	 * 是否支持处理 信息
	 * @param requestMessage
	 * @return
	 */
	public boolean isSupport(RequestMessage requestMessage);

	/**
	 * 处理请求信息
	 * @param requestMessage
	 * @return
	 */
	public ResponseMessage handle(RequestMessage requestMessage);
	
}