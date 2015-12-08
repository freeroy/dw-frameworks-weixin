package org.developerworld.frameworks.weixin.handler.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.handler.RequestMessageHandler;
import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;
import org.developerworld.frameworks.weixin.message.converter.RequestMessageConverter;
import org.developerworld.frameworks.weixin.message.converter.ResponseMessageConverter;

/**
 * 接收信息响应servlet
 * 
 * @author Roy Huang
 * @version 20150326
 * 
 */
public abstract class AbstractRequestMessageHandleServlet extends HttpServlet {

	protected RequestMessageConverter requestMessageConverter = new RequestMessageConverter();
	protected ResponseMessageConverter responseMessageConverter = new ResponseMessageConverter();

	private boolean isFindOtherHandlerWhenHasNotResponse = false;
	private boolean isOutPrintEmptyWhenHasNotResponse = true;

	public boolean isFindOtherHandlerWhenHasNotResponse() {
		return isFindOtherHandlerWhenHasNotResponse;
	}

	public void setFindOtherHandlerWhenHasNotResponse(boolean isFindOtherHandlerWhenHasNotResponse) {
		this.isFindOtherHandlerWhenHasNotResponse = isFindOtherHandlerWhenHasNotResponse;
	}

	public boolean isOutPrintEmptyWhenHasNotResponse() {
		return isOutPrintEmptyWhenHasNotResponse;
	}

	public void setOutPrintEmptyWhenHasNotResponse(boolean isOutPrintEmptyWhenHasNotResponse) {
		this.isOutPrintEmptyWhenHasNotResponse = isOutPrintEmptyWhenHasNotResponse;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config.getInitParameter("isFindOtherHandlerWhenHasNotResponse") != null)
			setFindOtherHandlerWhenHasNotResponse(
					Boolean.valueOf(config.getInitParameter("isFindOtherHandlerWhenHasNotResponse")));
		if (config.getInitParameter("isOutPrintEmptyWhenHasNotResponse") != null)
			setOutPrintEmptyWhenHasNotResponse(
					Boolean.valueOf(config.getInitParameter("isOutPrintEmptyWhenHasNotResponse")));
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
			throw new ServletException(e);
		}
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
		String xml = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
		String outXml = null;
		// 为防止微信超时重复发送请求，需先判断是否已经处理过该信息
		if (hasResponseMessageInCache(xml)) {
			// 从缓存读取响应信息
			outXml = getResponseMessageInCache(xml);
		} else {
			if (StringUtils.isNotBlank(xml)) {
				// 构造对象
				RequestMessage reqMessage = requestMessageConverter.convertToObject(xml);
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
								outXml = responseMessageConverter.convertToXml(_repMessage);
								if (StringUtils.isNotBlank(outXml))
									break;
							}
							// 若设置为不再继续查找，则跳出
							if (!isFindOtherHandlerWhenHasNotResponse())
								break;
						}
					}
				}
			}
			// 把响应信息写进缓存
			putResponseMessageInCache(xml, outXml);
		}
		if (outXml == null && isOutPrintEmptyWhenHasNotResponse())
			outXml = "";
		response.getWriter().print(outXml);
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
			validateStr = DigestUtils.shaHex(validateStr);
			// 执行验证
			rst = signature.equalsIgnoreCase(validateStr);
		}
		return rst;
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
	 * 对应请求的响应信息是否存在
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected boolean hasResponseMessageInCache(String requestMessage) {
		return false;
	}

	/**
	 * 获取请求对应的响应信息
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected String getResponseMessageInCache(String requestMessage) {
		throw new UnsupportedOperationException("该方法没有实现");
	}

	/**
	 * 缓存请求响应
	 * 
	 * @param requestMessage
	 * @param responseMessage
	 */
	protected void putResponseMessageInCache(String requestMessage, String responseMessage) {
		// ...
	}
}
