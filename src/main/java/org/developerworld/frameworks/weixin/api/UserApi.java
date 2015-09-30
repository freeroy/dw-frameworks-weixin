package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.developerworld.frameworks.weixin.api.dto.User;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户api
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class UserApi {

	private final static String GET_USER_API = "https://api.weixin.qq.com/cgi-bin/user/info";

	private final static String GET_USERS_API = "https://api.weixin.qq.com/cgi-bin/user/get";

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";
	
	private final static int MAX_GET_USER_COUNT=10000;

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param lang
	 * @param result
	 * @throws IOException
	 */
	public static void getUser(String accessToken, String openId, String lang,
			Map result) throws IOException {
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
	 * @throws IOException
	 */
	public static void getUser(String accessToken, String openId, Map result)
			throws IOException {
		getUser(accessToken, openId, null, result);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @param lang
	 * @param result
	 * @throws IOException
	 */
	public static User getUser(String accessToken, String openId, String lang)
			throws IOException {
		User rst = null;
		Map result = new HashMap();
		getUser(accessToken, openId, lang, result);
		if (result.get("subscribe") != null) {
			rst = new User();
			rst.setSubscribe(Byte.valueOf(result.get("subscribe").toString()));
			if (rst.getSubscribe().equals(User.SUBSCRIPE_SUBSCRIPE)) {
				rst.setCity((String) result.get("city"));
				rst.setCountry((String) result.get("country"));
				rst.setHeadImgUrl((String) result.get("headimgurl"));
				rst.setLanguage((String) result.get("language"));
				rst.setNickname((String) result.get("nickname"));
				rst.setOpenId((String) result.get("openid"));
				rst.setProvince((String) result.get("province"));
				rst.setSex(Byte.valueOf(result.get("sex").toString()));
				rst.setSubscribeTime(Long.valueOf(result.get("subscribe_time")
						.toString()));
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
	 * @throws IOException
	 */
	public static User getUser(String accessToken, String openId)
			throws IOException {
		return getUser(accessToken, openId, (String) null);
	}

	/**
	 * 获取用户数
	 * 
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static Long getUserCount(String accessToken) throws HttpException,
			IOException {
		Map result = new HashMap();
		getUsers(accessToken, null, result);
		if (result.get("total") != null)
			return Long.valueOf(result.get("total").toString());
		return null;
	}

	/**
	 * 获取用户数据
	 * @param accessToken
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<String> getUsers(String accessToken, int pageNum,
			int pageSize) throws HttpException, IOException {
		// 根据pageNum、pageSize.获取开始检索位置
		int bIndex = (pageNum - 1) * pageSize;
		// 由于每次最大检索1W，所以需要知道要执行多少次接口操作
		int callTime = bIndex / MAX_GET_USER_COUNT + 1;
		String nextOpenId = null;
		Map result = null;
		while (callTime > 0) {
			--callTime;
			result = new HashMap();
			getUsers(accessToken, nextOpenId, result);
			nextOpenId = (String) result.get("next_openid");
			// 若已经不能再向后获取，就退出
			if (nextOpenId == null)
				break;
		}
		// 若没完成预计的执行次数，代表无法获取指定分页条件的数据，返回空集合
		if (callTime > 0)
			return new ArrayList<String>();
		else {
			List<String> users = new ArrayList<String>();
			if (result.get("data") != null && result.get("data") instanceof Map) {
				Object openidObj = ((Map) result.get("data")).get("openid");
				if (openidObj != null && openidObj instanceof Collection) {
					Collection openIdColl = (Collection) openidObj;
					List openIdsList = null;
					if (!(openIdColl instanceof List))
						openIdsList = new ArrayList(openIdColl);
					else
						openIdsList = (List) openIdColl;
					// 根据分页参数，计算在本《=1W条数据中，提取pagesize条
					int begin = bIndex % MAX_GET_USER_COUNT;
					int end = Math.min(begin + pageSize, openIdColl.size());
					for (int i = begin; i < end; i++)
						users.add(openIdsList.get(i).toString());
				}
			}
			return users;
		}
	}

	/**
	 * 获取关注用户信息
	 * 
	 * @param accessToken
	 * @param nextOpenId
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getUsers(String accessToken, String nextOpenId,
			Map result) throws HttpException, IOException {
		String url = GET_USERS_API + "?access_token=" + accessToken;
		if (nextOpenId != null)
			url += "&next_openid=" + nextOpenId;
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
	 * 获取关注用户信息
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getUsers(String accessToken, Map result)
			throws HttpException, IOException {
		getUsers(accessToken, null, result);
	}

	/**
	 * 获取关注用户信息
	 * 
	 * @param accessToken
	 * @param nextOpenId
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<String> getUsers(String accessToken, String nextOpenId)
			throws HttpException, IOException {
		List<String> rst = new ArrayList<String>();
		Map result = new HashMap();
		getUsers(accessToken, nextOpenId, result);
		if (result.get("data") != null && result.get("data") instanceof Map) {
			Object openid = ((Map) result.get("data")).get("openid");
			if (openid != null && openid instanceof Collection) {
				for (Object data : (Collection) openid)
					rst.add(data.toString());
			}
		}
		return rst;
	}

	/**
	 * 获取关注用户信息
	 * 
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<String> getUsers(String accessToken)
			throws HttpException, IOException {
		return getUsers(accessToken, (String) null);
	}
}
