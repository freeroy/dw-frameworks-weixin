package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.developerworld.frameworks.weixin.api.dto.AuthUser;
import org.developerworld.frameworks.weixin.api.dto.WebAuthScope;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 网页授权api
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class WebAuthApi {

	private final static String AUTH_API = "https://open.weixin.qq.com/connect/oauth2/authorize";
	private final static String GET_TOKEN_API = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private final static String REFRESH_TOKEN_API = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	private final static String GET_USER_API = "https://api.weixin.qq.com/sns/userinfo";

	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static String REQUEST_CONTENT_CHARSET="UTF-8";

	/**
	 * 获取授权地址
	 * 
	 * @param appId
	 * @param redirectUri
	 * @param scope
	 * @param state
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getAuthUrl(String appId, String redirectUri,
			WebAuthScope scope, String state)
			throws UnsupportedEncodingException {
		String rst = AUTH_API + "?appid=" + appId + "&redirect_uri="
				+ URLEncoder.encode(redirectUri, "utf-8")
				+ "&response_type=code" + "&scope=" + scope.toString() + "&state=" + state
				+ "#wechat_redirect";
		return rst;
	}

	/**
	 * 获取access token
	 * 
	 * @param appId
	 * @param secret
	 * @param code
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getAccessToken(String appId, String secret, String code,
			Map result) throws HttpException, IOException {
		String url = GET_TOKEN_API + "?appid=" + appId + "&secret=" + secret
				+ "&code=" + code + "&grant_type=authorization_code";
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 获取access token
	 * 
	 * @param appId
	 * @param secret
	 * @param code
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String getAccessToken(String appId, String secret, String code)
			throws HttpException, IOException {
		String rst = null;
		Map result = new HashMap();
		getAccessToken(appId, secret, code, result);
		if (result.get("access_token") != null)
			rst = (String) result.get("access_token");
		return rst;
	}

	/**
	 * 刷新access token
	 * 
	 * @param appId
	 * @param refreshToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void refreshAccessToken(String appId, String refreshToken,
			Map result) throws HttpException, IOException {
		String url = REFRESH_TOKEN_API + "?appid=" + appId
				+ "&grant_type=refresh_token" + "&refresh_token="
				+ refreshToken;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 刷新access token
	 * 
	 * @param appId
	 * @param refreshToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String refreshAccessToken(String appId, String refreshToken)
			throws HttpException, IOException {
		String rst = null;
		Map result = new HashMap();
		refreshAccessToken(appId, refreshToken, result);
		if (result.get("access_token") != null)
			rst = (String) result.get("access_token");
		return rst;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param lang
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getUser(String accessToken, String openId, String lang,
			Map result) throws HttpException, IOException {
		String url = GET_USER_API + "?access_token=" + accessToken + "&openid="
				+ openId + "&lang="
				+ (lang == null ? Locale.getDefault() : lang);
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getUser(String accessToken, String openId, Map result)
			throws HttpException, IOException {
		getUser(accessToken, openId, null, result);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param lang
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static AuthUser getUser(String accessToken, String openId,
			String lang) throws HttpException, IOException {
		AuthUser rst = null;
		Map result = new HashMap();
		getUser(accessToken, openId, lang, result);
		if (result.get("openid") != null) {
			rst = new AuthUser();
			rst.setCity((String) result.get("city"));
			rst.setCountry((String) result.get("country"));
			rst.setHeadImgUrl((String) result.get("headimgurl"));
			rst.setNickname((String) result.get("nickname"));
			rst.setOpenId((String) result.get("openid"));
			rst.setProvince((String) result.get("province"));
			rst.setSex(Byte.valueOf(result.get("sex").toString()));
			if (result.get("privilege") != null
					&& result.get("privilege") instanceof Collection) {
				for (Object data : (Collection) result.get("privilege"))
					rst.getPrivilege().add(data.toString());
			}
		}
		return rst;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static AuthUser getUser(String accessToken, String openId)
			throws HttpException, IOException {
		return getUser(accessToken, openId, (String) null);
	}

}
