package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.developerworld.frameworks.weixin.api.dto.Group;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户组api
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class GroupApi {

	private final static String GROUP_CREATE_API = "https://api.weixin.qq.com/cgi-bin/groups/create";
	private final static String GROUP_UPDATE_API = "https://api.weixin.qq.com/cgi-bin/groups/update";
	private final static String GROUP_LIST_API = "https://api.weixin.qq.com/cgi-bin/groups/get";
	private final static String USER_GROUP_API = "https://api.weixin.qq.com/cgi-bin/groups/getid";
	private final static String USER_GROUP_UPDATE_API = "https://api.weixin.qq.com/cgi-bin/groups/members/update";

	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static String REQUEST_CONTENT_CHARSET="UTF-8";

	/**
	 * 创建分组
	 * 
	 * @param accessToken
	 * @param name
	 * @param result
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void createGroup(String accessToken, String name, Map result)
			throws HttpException, IOException {
		String url = GROUP_CREATE_API + "?access_token=" + accessToken;
		Map jsonM1 = new LinkedHashMap();
		jsonM1.put("name", name);
		Map jsonM2 = new LinkedHashMap();
		jsonM2.put("group", jsonM1);
		String jsonStr = objectMapper.writeValueAsString(jsonM2);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr,
					"application/x-www-form-urlencoded", "utf-8");
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
	 * 创建分组
	 * 
	 * @param accessToken
	 * @param name
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static Integer createGroup(String accessToken, String name)
			throws HttpException, IOException {
		Integer rst = null;
		Map result = new HashMap();
		createGroup(accessToken, name, result);
		if (result.get("group") != null && result.get("group") instanceof Map) {
			rst = (Integer) ((Map) result.get("group")).get("id");
		}
		return rst;
	}

	/**
	 * 获取分组信息
	 * 
	 * @param accessToken
	 * @param result
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void getGroups(String accessToken, Map result)
			throws HttpException, IOException {
		String url = GROUP_LIST_API + "?access_token=" + accessToken;
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
	 * 获取分组信息
	 * 
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<Group> getGroups(String accessToken)
			throws HttpException, IOException {
		List<Group> rst = new ArrayList<Group>();
		Map result = new HashMap();
		getGroups(accessToken, result);
		if (result.get("groups") != null
				&& result.get("groups") instanceof Collection) {
			Collection groups = (Collection) result.get("groups");
			for (Object group : groups) {
				Map _group = (Map) group;
				Group data = new Group();
				data.setId(Integer.valueOf(_group.get("id").toString()));
				data.setName(_group.get("name").toString());
				data.setCount(Long.valueOf(_group.get("count").toString()));
				rst.add(data);
			}
		}
		return rst;
	}

	/**
	 * 查询用户组信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param result
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static void getUserGroup(String accessToken, String openId,
			Map result) throws JsonGenerationException, JsonMappingException,
			IOException {
		String url = USER_GROUP_API + "?access_token=" + accessToken;
		Map jsonM = new LinkedHashMap();
		jsonM.put("openid", openId);
		String jsonStr = objectMapper.writeValueAsString(jsonM);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr,
					"application/x-www-form-urlencoded", "utf-8");
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
	 * 获取用户组信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static Integer getUserGroup(String accessToken, String openId)
			throws JsonGenerationException, JsonMappingException, IOException {
		Integer rst = null;
		Map result = new HashMap();
		getUserGroup(accessToken, openId, result);
		if (result.get("groupid") != null)
			rst = Integer.valueOf(result.get("groupid").toString());
		return rst;
	}

	/**
	 * 修改分组名
	 * 
	 * @param accessToken
	 * @param groupId
	 * @param name
	 * @param result
	 * @throws IOException
	 * @throw JsonMappingException
	 * @throws JsonGenerationExceptio
	 */
	public static void updateGroupName(String accessToken, Integer groupId,
			String name, Map result) throws JsonGenerationException,
			JsonMappingException, IOException {
		String url = GROUP_UPDATE_API + "?access_token=" + accessToken;
		Map jsonM1 = new LinkedHashMap();
		jsonM1.put("id", groupId);
		jsonM1.put("name", name);
		Map jsonM2 = new LinkedHashMap();
		jsonM2.put("group", jsonM1);
		String jsonStr = objectMapper.writeValueAsString(jsonM2);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr,
					"application/x-www-form-urlencoded", "utf-8");
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
	 * 更新组名称
	 * 
	 * @param accessToken
	 * @param groupId
	 * @param name
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static boolean updateGroupName(String accessToken, Integer groupId,
			String name) throws JsonGenerationException, JsonMappingException,
			IOException {
		boolean rst = false;
		Map result = new HashMap();
		updateGroupName(accessToken, groupId, name, result);
		if (result.get("errcode") != null)
			rst=result.get("errcode").toString().equals("0");
		return rst;
	}

	/**
	 * 移动用户分组
	 * 
	 * @param accessToken
	 * @param openId
	 * @param groupId
	 * @param result
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static void updateUserGroup(String accessToken, String openId,
			Integer groupId, Map result) throws JsonGenerationException,
			JsonMappingException, IOException {
		String url = USER_GROUP_UPDATE_API + "?access_token=" + accessToken;
		Map jsonM = new LinkedHashMap();
		jsonM.put("openid", openId);
		jsonM.put("to_groupid", groupId);
		String jsonStr = objectMapper.writeValueAsString(jsonM);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr,
					"application/x-www-form-urlencoded", "utf-8");
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
	 * 移动用户分组s
	 * 
	 * @param accessToken
	 * @param openId
	 * @param groupId
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static boolean updateUserGroup(String accessToken, String openId,
			Integer groupId) throws JsonGenerationException,
			JsonMappingException, IOException {
		boolean rst = false;
		Map result = new HashMap();
		updateUserGroup(accessToken, openId, groupId, result);
		if (result.get("errcode") != null)
			rst=result.get("errcode").toString().equals("0");
		return rst;
	}
}
