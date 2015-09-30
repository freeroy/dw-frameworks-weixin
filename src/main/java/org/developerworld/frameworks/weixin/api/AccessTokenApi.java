package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * token api
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class AccessTokenApi {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static String TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token";
	
	private final static String REQUEST_CONTENT_CHARSET="UTF-8";

	/**
	 * 获取access token
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 * @throws IOException
	 */
	public static String getAccessToken(String appid, String secret)
			throws IOException {
		Map result = new HashMap();
		getAccessToken(appid, secret, result);
		if (result.get("access_token") != null)
			return (String) result.get("access_token");
		return null;
	}

	/**
	 * 执行access_token获取
	 * 
	 * @param appid
	 * @param secret
	 * @param result
	 * @throws IOException
	 */
	public static void getAccessToken(String appid, String secret, Map result)
			throws IOException {
		String grantType = "client_credential";
		String url = TOKEN_API + "?grant_type=" + grantType + "&appid=" + appid
				+ "&secret=" + secret;
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

}
