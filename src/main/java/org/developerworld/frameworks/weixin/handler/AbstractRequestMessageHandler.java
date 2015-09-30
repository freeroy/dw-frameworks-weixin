package org.developerworld.frameworks.weixin.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;

/**
 * 抽象泛型请求信息处理器
 * 
 * @author Roy Huang
 * @version 20140310
 * 
 */
public abstract class AbstractRequestMessageHandler<T extends RequestMessage>
		implements RequestMessageHandler {

	private Class<T> requestMessageClass;

	public AbstractRequestMessageHandler() {
		Type type = getClass().getGenericSuperclass();
		while (type != null && !(type instanceof ParameterizedType))
			type = type.getClass().getGenericSuperclass();
		if (type != null && type instanceof ParameterizedType) {
			Type types[] = ((ParameterizedType) type).getActualTypeArguments();
			if (types != null && types.length > 0 && types[0] instanceof Class)
				requestMessageClass = (Class<T>) types[0];
		}
	}

	public boolean isSupport(RequestMessage requestMessage) {
		if (requestMessage != null
				&& requestMessageClass.isInstance(requestMessage))
			return isSupportGeneric((T) requestMessage);
		return false;
	}

	public ResponseMessage handle(RequestMessage requestMessage) {
		return handleGeneric((T) requestMessage);
	}

	/**
	 * 是否支持指定类型的信息
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected abstract boolean isSupportGeneric(T requestMessage);

	/**
	 * 是否支持指定类型的信息
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected abstract ResponseMessage handleGeneric(
			T requestMessage);

}