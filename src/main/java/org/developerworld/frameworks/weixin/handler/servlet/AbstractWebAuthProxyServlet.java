package org.developerworld.frameworks.weixin.handler.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.api.WebAuthApi;
import org.developerworld.frameworks.weixin.api.dto.AuthUser;
import org.developerworld.frameworks.weixin.api.dto.WebAuthScope;

/**
 * web授权获取Servlet
 * 
 * @author Roy Huang
 * @version 20140313
 * 
 */
public abstract class AbstractWebAuthProxyServlet extends HttpServlet {

	/**
	 * 回调请求接收
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws HttpException, IOException {
		String accessType = request
				.getParameter(getAccessTypeRequestParameterName(request));
		if (StringUtils.isNotBlank(accessType)
				&& accessType.trim().equalsIgnoreCase("update")) {
			updateAccessToken(request);
		}
		Map accessToken = getAccessTokenInCache(request);
		// 若存在授权信息，检查其是否过期
		if (accessToken != null) {
			Date getDate = (Date) accessToken.get("get_date");
			int expiresIn = Integer.parseInt(accessToken.get("expires_in")
					.toString());
			// 判断是否过期
			if (getDate.getTime() + (expiresIn * 1000) <= System
					.currentTimeMillis()) {
				String appId = getAppId(request);
				String refreshToken = (String) accessToken.get("refresh_token");
				Map result = new HashMap();
				// 执行刷新缓存操作
				result.put("get_date", new Date());
				WebAuthApi.refreshAccessToken(appId, refreshToken, result);
				// 若获取失败，则当无accessToken
				if (result.containsKey("access_token"))
					accessToken = result;
				else
					accessToken = null;
				putAccessTokenInCache(request, accessToken);
			}
		}
		if (accessToken != null)
			handleAccessToken(request, response, accessToken);
		else if (StringUtils.isNotBlank(accessType)
				&& accessType.trim().equalsIgnoreCase("update"))
			handleWebAuthDisable(request, response);
		else
			handleAccessTokenNotFound(request, response);
	}

	/**
	 * 更新accessToken
	 * 
	 * @param request
	 * @throws HttpException
	 * @throws IOException
	 */
	private void updateAccessToken(HttpServletRequest request)
			throws HttpException, IOException {
		String code = request.getParameter("code");
		// 用户不禁止授权
		if (StringUtils.isNotBlank(code)) {
			Map result = new HashMap();
			result.put("get_date", new Date());
			String appId = getAppId(request);
			String secret = getSecret(request);
			WebAuthApi.getAccessToken(appId, secret, code, result);
			// 成功获取accessToken
			if (StringUtils.isNotBlank((String) result.get("access_token")))
				putAccessTokenInCache(request, result);
		}
	}

	/**
	 * 获取用户信息国际化
	 * 
	 * @param request
	 * @return
	 */
	protected Locale getRequestUserLang(HttpServletRequest request) {
		return request.getLocale();
	}

	/**
	 * 处理accessToken
	 * 
	 * @param request
	 * @param response
	 * @param accessToken
	 * @throws IOException
	 * @throws HttpException
	 */
	protected void handleAccessToken(HttpServletRequest request,
			HttpServletResponse response, Map accessToken)
			throws HttpException, IOException {
		String _accessToken = (String) accessToken.get("access_token");
		String _openid = (String) accessToken.get("openid");
		String _scope = (String) accessToken.get("scope");
		handleAccessToken(request, response, _accessToken, _openid, _scope);
	}

	/**
	 * 处理accessToken数据(可根据实际需要，覆盖该方法)
	 * 
	 * @param request
	 * @param response
	 * @param accessToken
	 * @param openid
	 * @param scope
	 * @throws IOException
	 * @throws HttpException
	 */
	protected void handleAccessToken(HttpServletRequest request,
			HttpServletResponse response, String accessToken, String openid,
			String scope) throws HttpException, IOException {
		AuthUser authUser = null;
		// 根据openId，获取用户信息
		if (StringUtils.isNotBlank(openid)) {
			authUser = getAuthUserInCache(request, accessToken, openid,
					getRequestUserLang(request).toString());
			if (authUser == null) {
				authUser = WebAuthApi.getUser(accessToken, openid,
						getRequestUserLang(request).toString());
				putAuthUserInCache(request, accessToken, openid,
						getRequestUserLang(request).toString(), authUser);
			}
		}
		handleAuthUser(request, response, accessToken, openid, scope, authUser,
				request.getParameter("state"));
	}

	/**
	 * 当不存在access时，执行的操作(可以根据特性需要，覆盖该函数)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void handleAccessTokenNotFound(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String state = request.getParameter("state");
		String scope = request.getParameter("scope");
		WebAuthScope webAuthScope = WebAuthScope.SNSAPI_BASE.toString()
				.equalsIgnoreCase(scope) ? WebAuthScope.SNSAPI_BASE
				: WebAuthScope.SNSAPI_USERINFO;
		String url = getCurrentUrl(request);
		if (StringUtils.isNotBlank(request.getQueryString()))
			url += "&";
		else
			url += "?";
		url += getAccessTypeRequestParameterName(request) + "=update";
		url = WebAuthApi
				.getAuthUrl(getAppId(request), url, webAuthScope, state);
		// 从定向至微信
		response.sendRedirect(url);
	}

	/**
	 * 获取当前请求地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getCurrentUrl(HttpServletRequest request) {
		String rst = request.getScheme() + "://" + request.getServerName();
		int serverPort = request.getServerPort();
		if (serverPort > 0 && serverPort != 80 && serverPort != 8000
				&& serverPort != 443)
			rst += ":" + serverPort;
		rst += request.getRequestURI();
		if (StringUtils.isNotBlank(request.getQueryString()))
			rst += "?" + request.getQueryString();
		return rst;
	}

	/**
	 * 获取访问类型的请求参数名
	 * 
	 * @param request
	 * @return
	 */
	protected String getAccessTypeRequestParameterName(
			HttpServletRequest request) {
		return "access_type";
	}

	/**
	 * 处理授权用户信息
	 * 
	 * @param request
	 * @param response
	 * @param accessToken
	 * @param openid
	 * @param scope
	 * @param authUser
	 * @param state
	 */
	abstract protected void handleAuthUser(HttpServletRequest request,
			HttpServletResponse response, String accessToken, String openid,
			String scope, AuthUser authUser, String state);

	/**
	 * 用户不授权
	 * 
	 * @param request
	 * @param response
	 */
	abstract protected void handleWebAuthDisable(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 从请求上文中获取token(这里可按需要重载)
	 * 
	 * @param request
	 * @return
	 */
	abstract protected Map getAccessTokenInCache(HttpServletRequest request);

	/**
	 * 更新token信息至请求上下文(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 */
	abstract protected void putAccessTokenInCache(HttpServletRequest request,
			Map accessToken);

	/**
	 * 从缓存中获取授权用户信息(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 * @param openid
	 * @param string
	 * @return
	 */
	abstract protected AuthUser getAuthUserInCache(HttpServletRequest request,
			String accessToken, String openid, String string);

	/**
	 * 吧授权用户信息写入缓存(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 * @param openid
	 * @param string
	 * @param authUser
	 */
	abstract protected void putAuthUserInCache(HttpServletRequest request,
			String accessToken, String openid, String string, AuthUser authUser);

	/**
	 * 通过请求上下文获取appId
	 * 
	 * @param request
	 * @return
	 */
	abstract protected String getAppId(HttpServletRequest request);

	/**
	 * 通过请求上下文获取secret
	 * 
	 * @param request
	 * @return
	 */
	abstract protected String getSecret(HttpServletRequest request);

}
