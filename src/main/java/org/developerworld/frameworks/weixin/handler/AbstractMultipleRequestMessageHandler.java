package org.developerworld.frameworks.weixin.handler;

import java.util.LinkedHashSet;

import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;

/**
 * 多重组合请求信息处理器
 * 
 * @author Roy Huang
 * @version 20140310
 * 
 * @param <T>
 */
public abstract class AbstractMultipleRequestMessageHandler implements
		RequestMessageHandler {

	public boolean isSupport(RequestMessage requestMessage) {
		LinkedHashSet<RequestMessageHandler> requestMessageHandlers = getRequestMessageHandlers();
		int match = 0;
		for (RequestMessageHandler handler : requestMessageHandlers) {
			if (handler.isSupport(requestMessage)) {
				++match;
				if (!isMustAllSupport())
					break;
			}
		}
		return match == requestMessageHandlers.size()
				|| (match > 0 && !isMustAllSupport());
	}

	public ResponseMessage handle(RequestMessage requestMessage) {
		ResponseMessage rst = null;
		for (RequestMessageHandler handler : getRequestMessageHandlers()) {
			if (isMustAllSupport() || handler.isSupport(requestMessage)) {
				rst = handler.handle(requestMessage);
				if (rst != null && isSkipHandlerWhenHasResponse())
					break;
			}
		}
		return rst;
	}

	/**
	 * 获取请求信息处理器集合
	 * 
	 * @return
	 */
	protected abstract LinkedHashSet<RequestMessageHandler> getRequestMessageHandlers();

	/**
	 * 是否必须全部处理器都通过才能执行
	 * 
	 * @return
	 */
	protected abstract boolean isMustAllSupport();

	/**
	 * 是否当存在处理输出，则不再处理
	 * 
	 * @return
	 */
	protected abstract boolean isSkipHandlerWhenHasResponse();

}
