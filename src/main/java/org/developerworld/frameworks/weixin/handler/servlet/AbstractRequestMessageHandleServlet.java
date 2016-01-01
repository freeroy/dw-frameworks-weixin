package org.developerworld.frameworks.weixin.handler.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.developerworld.commons.cache.Cache;
import org.developerworld.frameworks.weixin.handler.RequestMessageHandler;
import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;
import org.developerworld.frameworks.weixin.message.converter.RequestMessageConverter;
import org.developerworld.frameworks.weixin.message.converter.ResponseMessageConverter;
import org.dom4j.DocumentException;

/**
 * 接收信息响应servlet
 * 
 * @author Roy Huang
 */
public abstract class AbstractRequestMessageHandleServlet extends HttpServlet {

	private final static Log LOG = LogFactory.getLog(AbstractRequestMessageHandleServlet.class);

	protected RequestMessageConverter requestMessageConverter = new RequestMessageConverter();
	protected ResponseMessageConverter responseMessageConverter = new ResponseMessageConverter();

	private boolean isFindOtherHandlerWhenHasNotResponse = false;
	private long waitLockRequestMessageTimeout = 5000;

	public boolean isFindOtherHandlerWhenHasNotResponse() {
		return isFindOtherHandlerWhenHasNotResponse;
	}

	public void setFindOtherHandlerWhenHasNotResponse(boolean isFindOtherHandlerWhenHasNotResponse) {
		this.isFindOtherHandlerWhenHasNotResponse = isFindOtherHandlerWhenHasNotResponse;
	}

	public void setWaitLockRequestMessageTimeout(long waitLockRequestMessageTimeout) {
		this.waitLockRequestMessageTimeout = waitLockRequestMessageTimeout;
	}

	public long getWaitLockRequestMessageTimeout() {
		return waitLockRequestMessageTimeout;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config.getInitParameter("isFindOtherHandlerWhenHasNotResponse") != null)
			setFindOtherHandlerWhenHasNotResponse(
					Boolean.valueOf(config.getInitParameter("isFindOtherHandlerWhenHasNotResponse")));
		if (config.getInitParameter("waitLockRequestMessageTimeout") != null)
			setWaitLockRequestMessageTimeout(Long.parseLong(config.getInitParameter("waitLockRequestMessageTimeout")));
	}

	/**
	 * 处理get请求
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = getToken(request);
		if (checkJoinUp(request, token))
			response.getWriter().print(request.getParameter("echostr"));
	}

	/**
	 * 处理post请求
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String token = getToken(request);
			// 进行接入校验
			if (checkJoinUp(request, token))
				handleRequestMessage(request, response, token);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	/**
	 * 检查接入 若子类需要增加自己的验证，可以重载方法
	 * 
	 * @param request
	 * @return
	 */
	protected boolean checkJoinUp(HttpServletRequest request, String token) {
		boolean rst = false;
		// 加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		if (StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)
				&& StringUtils.isNotBlank(token)) {
			// 构建验证数组
			String[] validateArr = new String[] { token, timestamp, nonce };
			// 字典排序
			Arrays.sort(validateArr);
			// 构建验证字符串
			String validateStr = "";
			for (String validate : validateArr)
				validateStr += validate;
			// sha1加密
			validateStr = DigestUtils.sha1Hex(validateStr);
			// 执行验证
			rst = signature.equalsIgnoreCase(validateStr);
		}
		return rst;
	}

	/**
	 * 处理请求信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void handleRequestMessage(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		// 获取微信提交过来的信息
		String requestMessageXml = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
		if (StringUtils.isBlank(requestMessageXml))
			return;
		String responseMessageXml = null;
		// 为防止微信超时重复发送请求，需先判断是否已经处理过该信息
		if (hasResponseMessageInCache(requestMessageXml))
			// 从缓存读取响应信息
			responseMessageXml = getResponseMessageInCache(requestMessageXml);
		if (responseMessageXml == null) {
			lockRequestMessageHandle(requestMessageXml);
			try {
				responseMessageXml = doHandle(request,token, requestMessageXml);
				// 把响应信息写进缓存
				putResponseMessageInCache(requestMessageXml, responseMessageXml);
			} finally {
				unlockRequestMessageHandle(requestMessageXml);
			}
		}
		response.getWriter().print(responseMessageXml);
	}

	/**
	 * 执行请求信息处理
	 * @param request
	 * @param token
	 * @param requestMessageXml
	 * @return
	 * @throws Exception
	 */
	protected String doHandle(HttpServletRequest request,String token, String requestMessageXml)
			throws Exception {
		String rst=null;
		// 构造对象
		RequestMessage reqMessage = requestMessageConverter.convertToObject(requestMessageXml);
		if (reqMessage != null) {
			// 获取处理器
			Set<RequestMessageHandler> requestMessageHandlers = getRequestMessageHandlers(request);
			for (RequestMessageHandler requestMessageHandler : requestMessageHandlers) {
				// 判断是否支持处理信息
				if (requestMessageHandler.isSupport(reqMessage)) {
					// 处理并反馈响应信息
					ResponseMessage _repMessage = requestMessageHandler.handle(reqMessage);
					if (_repMessage != null) {
						// 转化响应信息
						rst = responseMessageConverter.convertToXml(_repMessage);
						if (StringUtils.isNotBlank(rst))
							break;
					}
					// 若设置为不再继续查找，则跳出
					if (!isFindOtherHandlerWhenHasNotResponse())
						break;
				}
			}
		}
		// 如果为空，转为空白字符串
		rst = rst == null ? "" : rst;
		return rst;
	}

	/**
	 * 对应请求的响应信息是否存在
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected boolean hasResponseMessageInCache(String requestMessage) {
		// 获取请求信息对应的响应值
		String responseMessage = getResponseMessageInCache(requestMessage);
		// 若无法获取响应信息，但请求信息被锁定，则尝试锁定
		if (responseMessage == null && isLockRequestMessageHandle(requestMessage)) {
			try {
				// 若请求信息被锁定，则等候一段时间再读取
				Thread.sleep(getWaitLockRequestMessageTimeout());
				responseMessage = getResponseMessageInCache(requestMessage);
			} catch (Exception e) {
				LOG.error(e);
			}
		}
		return responseMessage != null;
	}

	/**
	 * 判断请求信息是否被锁定
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected boolean isLockRequestMessageHandle(String requestMessage) {
		try {
			Cache cache = getLockRequestMessageHandleCache();
			if (cache != null)
				return (Boolean) cache.get(requestMessage);
		} catch (Exception e) {
			LOG.error(e);
		}
		return false;
	}

	/**
	 * 锁定请求信息的处理
	 * 
	 * @param xml
	 */
	protected void lockRequestMessageHandle(String requestMessage) {
		try {
			Cache cache = getLockRequestMessageHandleCache();
			if (cache != null)
				cache.put(requestMessage, true);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	/**
	 * 解锁请求信息的处理
	 * 
	 * @param xml
	 */
	protected void unlockRequestMessageHandle(String requestMessage) {
		try {
			Cache cache = getLockRequestMessageHandleCache();
			if (cache != null)
				cache.remove(requestMessage);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	/**
	 * 获取请求对应的响应信息
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected String getResponseMessageInCache(String requestMessage) {
		try {
			Cache cache = getResponseMessageCache();
			if (cache != null)
				return (String) cache.get(requestMessage);
		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
	}

	/**
	 * 缓存请求响应
	 * 
	 * @param requestMessage
	 * @param responseMessage
	 */
	protected void putResponseMessageInCache(String requestMessage, String responseMessage) {
		try {
			Cache cache = getResponseMessageCache();
			if (cache != null)
				cache.put(requestMessage, responseMessage);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	protected abstract String getToken(HttpServletRequest request);

	/**
	 * 获取请求信息处理器集合
	 * 
	 * @param request
	 * @return
	 */
	protected abstract LinkedHashSet<RequestMessageHandler> getRequestMessageHandlers(HttpServletRequest request);

	/**
	 * 获取用于记录锁定请求信息处理的缓存
	 * 
	 * @return
	 */
	abstract protected Cache getLockRequestMessageHandleCache();

	/**
	 * 获取记录响应信息的缓存
	 * 
	 * @return
	 */
	abstract protected Cache getResponseMessageCache();
}
