package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.developerworld.frameworks.weixin.api.dto.JsSign;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 由于前段js调用的api
 * 
 * @author Roy Huang
 * @version 20150228
 * 
 */
public class JsApi {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static String GET_TICKET_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	/**
	 * 执行jsapi_ticket获取
	 * 
	 * @param accessToken
	 * @param result
	 * @throws IOException
	 */
	public static void getJsApiTicket(String accessToken, Map result)
			throws IOException {
		String url = GET_TICKET_API + "?access_token=" + accessToken
				+ "&type=jsapi";
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
	 * 执行jsapi_ticket获取
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static String getJsApiTicket(String accessToken) throws IOException {
		Map result = new HashMap();
		getJsApiTicket(accessToken, result);
		if (result.get("ticket") != null)
			return (String) result.get("ticket");
		return null;
	}

	/**
	 * 获取js签名
	 * 
	 * @param jsApiTicket
	 * @param url
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static JsSign getJsSign(String jsApiTicket, String url) {
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		return getJsSign(jsApiTicket,url,nonceStr,timestamp);
	}

	/**
	 * 获取js签名
	 * @param jsApiTicket
	 * @param url
	 * @param noncestr
	 * @param timestamp
	 * @return
	 */
	public static JsSign getJsSign(String jsApiTicket, String url,
			String noncestr, String timestamp) {
		// 注意这里参数名必须全部小写，且必须有序
		String tempString = "jsapi_ticket=" + jsApiTicket + "&noncestr="
				+ noncestr + "&timestamp=" + timestamp + "&url=" + url;
		// sha1加密
		String signature = DigestUtils.shaHex(tempString);
		//构造签名对象
		JsSign sign = new JsSign();
		sign.setUrl(url);
		sign.setTicket(jsApiTicket);
		sign.setNonceStr(noncestr);
		sign.setTimestamp(timestamp);
		sign.setSignature(signature);
		return sign;
	}

	
	
	
}
