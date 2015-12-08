package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 二维码api
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class QrCodeApi {

	private final static String CREATE_QR_CODE_API = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
	private final static String GET_QR_CODE_API = "http://mp.weixin.qq.com/cgi-bin/showqrcode";// 官方说是https，实测是http

	private final static Integer EXPIRE_SECONDS = 1800;

	private final static ObjectMapper objectMapper = new ObjectMapper();
	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	/**
	 * 创建临时二维码
	 * 
	 * @param accessToken
	 * @param sceneId
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void createTempQrCode(String accessToken, long sceneId, Map result)
			throws JsonGenerationException, JsonMappingException, IOException {
		createTempQrCode(accessToken, null, sceneId, result);
	}

	/**
	 * 创建临时二维码
	 * 
	 * @param accessToken
	 * @param expireSeconds
	 * @param sceneId
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void createTempQrCode(String accessToken, Integer expireSeconds, long sceneId, Map result)
			throws JsonGenerationException, JsonMappingException, IOException {
		_createQrCode(accessToken, expireSeconds, sceneId, null, "QR_SCENE", result);
	}

	/**
	 * 创建临时二维码
	 * 
	 * @param accessToken
	 * @param sceneId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String createTempQrCode(String accessToken, long sceneId)
			throws JsonGenerationException, JsonMappingException, IOException {
		return createTempQrCode(accessToken, null, sceneId);
	}

	/**
	 * 创建临时二位码
	 * 
	 * @param accessToken
	 * @param expireSeconds
	 * @param sceneId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String createTempQrCode(String accessToken, Integer expireSeconds, long sceneId)
			throws JsonGenerationException, JsonMappingException, IOException {
		return _createQrCode(accessToken, expireSeconds, sceneId, null, "QR_SCENE");
	}

	/**
	 * 创建二维码
	 * 
	 * @param accessToken
	 * @param sceneId
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void createQrCode(String accessToken, long sceneId, Map result)
			throws JsonGenerationException, JsonMappingException, IOException {
		_createQrCode(accessToken, null, sceneId, null, "QR_LIMIT_SCENE", result);
	}

	/**
	 * 创建二维码
	 * 
	 * @param accessToken
	 * @param sceneId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String createQrCode(String accessToken, long sceneId)
			throws JsonGenerationException, JsonMappingException, IOException {
		return _createQrCode(accessToken, null, sceneId, null, "QR_LIMIT_SCENE");
	}

	/**
	 * 创建二维码(永久字符串参数)，返回ticket
	 * 
	 * @param accessToken
	 * @param sceneId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String createQrCode(String accessToken, String sceneStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		return _createQrCode(accessToken, null, null, sceneStr, "QR_LIMIT_STR_SCENE");
	}

	/**
	 * 创建临时二维码
	 * 
	 * @param accessToken
	 * @param expireSeconds
	 * @param sceneId
	 * @param sceneStr
	 * @param actionName
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static String _createQrCode(String accessToken, Integer expireSeconds, Long sceneId, String sceneStr,
			String actionName) throws JsonGenerationException, JsonMappingException, IOException {
		String rst = null;
		Map result = new HashMap();
		_createQrCode(accessToken, expireSeconds, sceneId, sceneStr, actionName, result);
		if (result.get("ticket") != null)
			rst = (String) result.get("ticket");
		return rst;
	}

	/**
	 * 创建二维码
	 * 
	 * @param accessToken
	 * @param expireSeconds
	 * @param sceneId
	 * @param actionName
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static void _createQrCode(String accessToken, Integer expireSeconds, Long sceneId, String sceneStr,
			String actionName, Map result) throws JsonGenerationException, JsonMappingException, IOException {
		String url = CREATE_QR_CODE_API + "?access_token=" + accessToken;
		Map scene = new LinkedHashMap();
		if (StringUtils.isNotBlank(sceneStr))
			scene.put("scene_str", sceneStr);
		else if (sceneId != null)
			scene.put("scene_id", sceneId);
		Map actionInfo = new LinkedHashMap();
		actionInfo.put("scene", scene);
		Map root = new LinkedHashMap();
		if (actionName.equals("QR_SCENE")) {
			root.put("action_name", "QR_SCENE");
			root.put("expire_seconds", (expireSeconds == null ? EXPIRE_SECONDS : expireSeconds));
		} else {
			root.put("action_name", "QR_LIMIT_SCENE");
		}
		root.put("action_info", actionInfo);
		String jsonStr = objectMapper.writeValueAsString(root);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr, "application/x-www-form-urlencoded",
					"utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
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
	 * 获取Qrcode的链接
	 * 
	 * @param ticket
	 * @return
	 */
	public static String getQrCodeUrl(String ticket) {
		return GET_QR_CODE_API + "?ticket=" + ticket;
	}

	/**
	 * 获取二维码
	 * 
	 * @param ticket
	 * @param result
	 * @throws IOException
	 */
	public static byte[] getQrCode(String ticket) throws IOException {
		byte[] rst = null;
		String url = getQrCodeUrl(ticket);
		GetMethod method = new GetMethod(url);
		try {
			HttpClient httpClient = new HttpClient();
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK)
				rst = method.getResponseBody();
		} finally {
			method.releaseConnection();
		}
		return rst;
	}

}
