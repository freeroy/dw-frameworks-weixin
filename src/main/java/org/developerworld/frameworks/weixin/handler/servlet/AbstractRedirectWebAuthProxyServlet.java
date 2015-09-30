package org.developerworld.frameworks.weixin.handler.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.developerworld.frameworks.weixin.api.WebAuthApi;
import org.developerworld.frameworks.weixin.api.dto.AuthUser;
import org.developerworld.frameworks.weixin.api.dto.WebAuthScope;

/**
 * 重定向accessToken
 * 
 * @author Roy Huang
 * 
 */
public abstract class AbstractRedirectWebAuthProxyServlet extends
		AbstractWebAuthProxyServlet {

	private final static Log log = LogFactory
			.getLog(AbstractRedirectWebAuthProxyServlet.class);

	@Override
	protected void handleAccessToken(HttpServletRequest request,
			HttpServletResponse response, Map accessToken)
			throws HttpException, IOException {
		String url = getRedirectUrl(request);
		if (StringUtils.isNotBlank(url)) {
			String scope = request.getParameter("scope");
			String state = request.getParameter("state");
			String _accessToken = (String) accessToken.get("access_token");
			String _openid = (String) accessToken.get("openid");
			String _scope = (String) accessToken.get("scope");
			AuthUser authUser = null;
			// 若请求时，指名需要获取用户详细信息，则执行
			if (WebAuthScope.SNSAPI_USERINFO.toString().equalsIgnoreCase(scope)
					&& StringUtils.isNotBlank(_openid)) {
				authUser = getAuthUserInCache(request, _accessToken, _openid,
						getRequestUserLang(request).toString());
				if (authUser == null) {
					authUser = WebAuthApi.getUser(_accessToken, _openid,
							getRequestUserLang(request).toString());
					putAuthUserInCache(request, _accessToken, _openid,
							getRequestUserLang(request).toString(), authUser);
				}
			}
			String prefix = getRedirectRequestParameterNamePrefix(request);
			if (StringUtils.isBlank(prefix))
				prefix = "";
			String requestMethod = getRedirectMethod(request);
			if ("post".equalsIgnoreCase(requestMethod)) {
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(url);
				try {
					String charset = getRedirectMethodPostCharset(request);
					if (charset == null)
						charset = "utf-8";
					method.setRequestHeader("Content-Type",
							"application/x-www-form-urlencoded;charset="
									+ charset);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					if (_accessToken != null)
						params.add(new NameValuePair(prefix + "accessToken",
								_accessToken));
					if (_openid != null)
						params.add(new NameValuePair(prefix + "openid", _openid));
					if (_scope != null)
						params.add(new NameValuePair(prefix + "scope", _scope));
					if (state != null)
						params.add(new NameValuePair(prefix + "state", state));
					if (isPutAuthUserDataToRedirectParameter()
							&& authUser != null) {
						if (authUser.getCity() != null)
							params.add(new NameValuePair(prefix + "city",
									authUser.getCity()));
						if (authUser.getCountry() != null)
							params.add(new NameValuePair(prefix + "country",
									authUser.getCountry()));
						if (authUser.getHeadImgUrl() != null)
							params.add(new NameValuePair(prefix + "headImgUrl",
									authUser.getHeadImgUrl()));
						if (authUser.getNickname() != null)
							params.add(new NameValuePair(prefix + "nickName",
									authUser.getNickname()));
						if (authUser.getProvince() != null)
							params.add(new NameValuePair(prefix + "province",
									authUser.getProvince()));
						if (authUser.getSex() != null)
							params.add(new NameValuePair(prefix + "sex",
									authUser.getSex().toString()));
						if (authUser.getPrivilege() != null) {
							for (String privilege : authUser.getPrivilege())
								params.add(new NameValuePair(prefix
										+ "privilege", privilege));
						}
					}
					method.setRequestBody(params.toArray(new NameValuePair[0]));
					client.executeMethod(method);
				} finally {
					method.releaseConnection();
				}
			} else {
				String encoding = getRedirectMethodGetEncoding(request);
				if (encoding == null)
					encoding = "utf-8";
				if (url.indexOf("?") < 0)
					url += "?";
				else
					url += "&";
				if (_accessToken != null)
					url += prefix + "accessToken=" + _accessToken + "&";
				if (_openid != null)
					url += prefix + "openid=" + _openid + "&";
				if (scope != null)
					url += prefix + "scope=" + _scope + "&";
				if (state != null)
					url += prefix + "state="
							+ URLEncoder.encode(state, encoding) + "&";
				if (isPutAuthUserDataToRedirectParameter() && authUser != null) {
					if (authUser.getCity() != null)
						url += prefix
								+ "city="
								+ URLEncoder.encode(authUser.getCity(),
										encoding) + "&";
					if (authUser.getCountry() != null)
						url += prefix
								+ "country="
								+ URLEncoder.encode(authUser.getCountry(),
										encoding) + "&";
					if (authUser.getHeadImgUrl() != null)
						url += prefix
								+ "headImgUrl="
								+ URLEncoder.encode(authUser.getHeadImgUrl(),
										encoding) + "&";
					if (authUser.getNickname() != null)
						url += prefix
								+ "nickName="
								+ URLEncoder.encode(authUser.getNickname(),
										encoding) + "&";
					if (authUser.getProvince() != null)
						url += prefix
								+ "province="
								+ URLEncoder.encode(authUser.getProvince(),
										encoding) + "&";
					if (authUser.getSex() != null)
						url += prefix + "sex=" + authUser.getSex().toString()
								+ "&";
					if (authUser.getPrivilege() != null) {
						for (String privilege : authUser.getPrivilege())
							url += prefix + "privilege="
									+ URLEncoder.encode(privilege, encoding)
									+ "&";
					}
				}
				if (url.endsWith("&"))
					url.substring(0, url.length() - 1);
				response.sendRedirect(url);
			}
		}
	}

	protected void handleWebAuthDisable(HttpServletRequest request,
			HttpServletResponse response) {
		String url = getRedirectUrl(request);
		if (StringUtils.isNotBlank(url)) {
			if (url.indexOf("?") < 0)
				url += "?";
			else
				url += "&";
			url += "state=" + request.getParameter("state");
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	/**
	 * 获取redirect时，参数名前缀(参数名参考微信回调)
	 * 
	 * @param request
	 * 
	 * @return
	 */
	protected String getRedirectRequestParameterNamePrefix(
			HttpServletRequest request) {
		return "";
	}

	/**
	 * 当redirect时，是否包含获取到的详细用户信息
	 * 
	 * @return
	 */
	protected boolean isPutAuthUserDataToRedirectParameter() {
		return true;
	}

	/**
	 * redirect时，所使用的method（get/post）
	 * 
	 * @param request
	 * @return
	 */
	protected String getRedirectMethod(HttpServletRequest request) {
		return "get";
	}

	/**
	 * redirect时，所使用的method 为post时，所使用的编码
	 * 
	 * @param request
	 * @return
	 */
	protected String getRedirectMethodPostCharset(HttpServletRequest request) {
		return StringUtils.isNotBlank(request.getCharacterEncoding()) ? request
				.getCharacterEncoding() : "utf-8";
	}

	/**
	 * redirect时，所使用的method 为get时，所使用的编码
	 * 
	 * @param request
	 * @return
	 */
	protected String getRedirectMethodGetEncoding(HttpServletRequest request) {
		return StringUtils.isNotBlank(request.getCharacterEncoding()) ? request
				.getCharacterEncoding() : "utf-8";
	}

	protected void handleAuthUser(HttpServletRequest request,
			HttpServletResponse response, String accessToken, String openid,
			String scope, AuthUser authUser, String state) {
		// 这里什么都不用做
	}

	/**
	 * 从请求上文中获取token(这里可按需要重载)
	 * 
	 * @param request
	 * @return
	 */
	protected Map getAccessTokenInCache(HttpServletRequest request) {
		Map rst = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			Map accessTokenData = (Map) session.getAttribute(getClass()
					.getName() + "_accessTokenData");
			if (accessTokenData != null) {
				String redirectUri = request.getParameter("redirect_uri");
				rst = (Map) accessTokenData.get(redirectUri);
			}
		}
		return rst;
	}

	/**
	 * 更新token信息至请求上下文(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 */
	protected void putAccessTokenInCache(HttpServletRequest request,
			Map accessToken) {
		HttpSession session = request.getSession(true);
		Map accessTokenData = (Map) session.getAttribute(getClass().getName()
				+ "_accessTokenData");
		if (accessTokenData == null) {
			accessTokenData = new HashMap();
			session.setAttribute(getClass().getName() + "_accessTokenData",
					accessTokenData);
		}
		String redirectUri = request.getParameter("redirect_uri");
		accessTokenData.put(redirectUri, accessToken);
	}

	/**
	 * 从缓存中获取授权用户信息(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 * @param openid
	 * @param string
	 * @return
	 */
	protected AuthUser getAuthUserInCache(HttpServletRequest request,
			String accessToken, String openid, String string) {
		AuthUser rst = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			Map authUserData = (Map) session.getAttribute(getClass().getName()
					+ "_authUserData");
			if (authUserData != null) {
				String redirectUri = request.getParameter("redirect_uri");
				rst = (AuthUser) authUserData.get(redirectUri);
			}
		}
		return rst;
	}

	/**
	 * 吧授权用户信息写入缓存(这里可按需要重载)
	 * 
	 * @param request
	 * @param accessToken
	 * @param openid
	 * @param string
	 * @param authUser
	 */
	protected void putAuthUserInCache(HttpServletRequest request,
			String accessToken, String openid, String string, AuthUser authUser) {
		HttpSession session = request.getSession(true);
		Map authUserData = (Map) session.getAttribute(getClass().getName()
				+ "_authUserData");
		if (authUserData == null) {
			authUserData = new HashMap();
			session.setAttribute(getClass().getName() + "_authUserData",
					authUserData);
		}
		String redirectUri = request.getParameter("redirect_uri");
		authUserData.put(redirectUri, authUser);
	}

	/**
	 * 获取dispatcher目标地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getRedirectUrl(HttpServletRequest request) {
		return request.getParameter("redirect_uri");
	}

}
