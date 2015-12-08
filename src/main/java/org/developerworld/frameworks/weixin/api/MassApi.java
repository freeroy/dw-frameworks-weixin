package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.developerworld.frameworks.weixin.api.dto.NewsArticle;
import org.developerworld.frameworks.weixin.message.CsMessage;
import org.developerworld.frameworks.weixin.message.MassMessage;
import org.developerworld.frameworks.weixin.message.converter.CsMessageConverter;
import org.developerworld.frameworks.weixin.message.converter.MassMessageConverter;
import org.developerworld.frameworks.weixin.message.mass.CsNewsMassMessage;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 群发API
 * 
 * @author Roy Huang
 * 
 */
public class MassApi {

	
	private final static String UPLOAD_NEWS_API = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
	private final static String UPLOAD_VIDEO_API = "https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo";
	private final static String SENDALL_MASS_API = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
	private final static String USER_MASS_API = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
	private final static String DELETE_MASS_API = "https://api.weixin.qq.com//cgi-bin/message/mass/delete";
	private final static String PREVIEW_MASS_API = "https://api.weixin.qq.com/cgi-bin/message/mass/preview";
	private final static String GET_MASS_MESSAGE_STATUS_API = "https://api.weixin.qq.com/cgi-bin/message/mass/get";

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static MassMessageConverter massMessageConverter = new MassMessageConverter();
	
	private final static CsMessageConverter csMessageConverter = new CsMessageConverter();

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	/**
	 * 上传图文素材
	 * 
	 * @param accessToken
	 * @param articles
	 * @return
	 * @throws IOException
	 */
	public static String uploadNews(String accessToken,
			List<NewsArticle> articles) throws IOException {
		String rst = null;
		Map result = new HashMap();
		uploadNews(accessToken, articles, result);
		return (String) result.get("media_id");
	}

	/**
	 * 上传图文素材
	 * 
	 * @param accessToken
	 * @param articles
	 * @param result
	 * @throws IOException
	 */
	public static void uploadNews(String accessToken,
			List<NewsArticle> articles, Map result) throws IOException {
		String url = UPLOAD_NEWS_API + "?access_token=" + accessToken;
		// 构造传递参数
		Map root = new LinkedHashMap();
		if (articles != null) {
			List<Map> datas = new ArrayList<Map>();
			for (NewsArticle article : articles) {
				Map data = new LinkedHashMap();
				data.put("thumb_media_id", article.getThumbMediaId());
				if (article.getAuthor() != null)
					data.put("author", article.getAuthor());
				data.put("title", article.getTitle());
				if (article.getContentSourceUrl() != null)
					data.put("content_source_url",
							article.getContentSourceUrl());
				data.put("content", article.getContent());
				if (article.getDigest() != null)
					data.put("digest", article.getDigest());
				if (article.getShowCoverPic() != null)
					data.put("show_cover_pic", article.getShowCoverPic());
				datas.add(data);
			}
			root.put("articles", datas);
		}
		String jsonStr = objectMapper.writeValueAsString(root);
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
	 * 上传视频
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param title
	 * @param description
	 * @return
	 * @throws IOException
	 */
	public static String uploadVideo(String accessToken, String mediaId,
			String title, String description) throws IOException {
		Map result = new HashMap();
		uploadVideo(accessToken, mediaId, title, description, result);
		return (String) result.get("media_id");
	}

	/**
	 * 上传视频
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param title
	 * @param description
	 * @param result
	 * @throws IOException
	 */
	public static void uploadVideo(String accessToken, String mediaId,
			String title, String description, Map result) throws IOException {
		String url = UPLOAD_VIDEO_API + "?access_token=" + accessToken;
		// 构造传递参数
		Map root = new LinkedHashMap();
		root.put("media_id", mediaId);
		root.put("title", title);
		root.put("description", description);
		String jsonStr = objectMapper.writeValueAsString(root);
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
	 * 发全员群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @return
	 * @throws IOException
	 */
	public static Long sendAllMessage(String accessToken,
			MassMessage massMessage) throws IOException {
		Long rst = null;
		Map result = new HashMap();
		sendAllMessage(accessToken, massMessage, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 发全员群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param result
	 * @throws IOException
	 */
	public static void sendAllMessage(String accessToken,
			MassMessage massMessage, Map result) throws IOException {
		String url = SENDALL_MASS_API + "?access_token=" + accessToken;
		String content = massMessageConverter.convertToAllJson(massMessage);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 发组群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param groupId
	 * @param result
	 * @throws IOException
	 */
	public static Long sendGroupMessage(String accessToken,
			MassMessage massMessage, String groupId) throws IOException {
		Long rst = null;
		Map result = new HashMap();
		sendGroupMessage(accessToken, massMessage, groupId, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 发组群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param groupId
	 * @param result
	 * @throws IOException
	 */
	public static void sendGroupMessage(String accessToken,
			MassMessage massMessage, String groupId, Map result)
			throws IOException {
		String url = SENDALL_MASS_API + "?access_token=" + accessToken;
		String content = massMessageConverter.convertToGroupJson(massMessage,
				groupId);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 发粉丝群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param openIds
	 * @return
	 * @throws IOException
	 */
	public static Long sendUsersMessage(String accessToken,
			MassMessage massMessage, List<String> openIds) throws IOException {
		Long rst = null;
		Map result = new HashMap();
		sendUsersMessage(accessToken, massMessage, openIds, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 发粉丝群发信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param openIds
	 * @param result
	 * @throws IOException
	 */
	public static void sendUsersMessage(String accessToken,
			MassMessage massMessage, List<String> openIds, Map result)
			throws IOException {
		String url = USER_MASS_API + "?access_token=" + accessToken;
		String content = massMessageConverter.convertToToUsersJson(massMessage,
				openIds);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 删除群发信息
	 * 
	 * @param accessToken
	 * @param msgId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static boolean deleteMass(String accessToken, Long msgId)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map result = new HashMap();
		deleteMass(accessToken, msgId, result);
		return result.get("errcode") != null && result.get("errcode").equals(0);
	}

	/**
	 * 删除群发信息
	 * 
	 * @param accessToken
	 * @param msgId
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void deleteMass(String accessToken, Long msgId, Map result)
			throws JsonGenerationException, JsonMappingException, IOException {
		String url = DELETE_MASS_API + "?access_token=" + accessToken;
		Map root = new LinkedHashMap();
		root.put("msgid", "msgId");
		String content = objectMapper.writeValueAsString(root);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 预览信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	public static Long previewMessage(String accessToken,
			MassMessage massMessage, String openId) throws IOException {
		Long rst = null;
		Map result = new HashMap();
		previewMessage(accessToken, massMessage, openId, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 预览信息
	 * 
	 * @param accessToken
	 * @param massMessage
	 * @param openId
	 * @param result
	 * @throws IOException
	 */
	public static void previewMessage(String accessToken,
			MassMessage massMessage, String openId, Map result)
			throws IOException {
		String url = PREVIEW_MASS_API + "?access_token=" + accessToken;
		String jsonStr = massMessageConverter.convertToToUserJson(massMessage,
				openId);
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
	 * 获取群发信息状态
	 * 
	 * @param accessToken
	 * @param msgId
	 * @return
	 * @throws IOException
	 */
	public static boolean getMassMessageStatus(String accessToken, String msgId)
			throws IOException {
		Map result = new HashMap();
		getMassMessageStatus(accessToken, msgId, result);
		return result.get("msg_status") != null
				&& result.get("msg_status").equals("SEND_SUCCESS");
	}

	/**
	 * 获取群发信息状态
	 * 
	 * @param accessToken
	 * @param msgId
	 * @param result
	 * @throws IOException
	 */
	public static void getMassMessageStatus(String accessToken, String msgId,
			Map result) throws IOException {
		String url = GET_MASS_MESSAGE_STATUS_API + "?access_token="
				+ accessToken;
		Map jsonM = new HashMap();
		jsonM.put("msg_id", msgId);
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
	 * 群发组信息
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param message
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static Long sendGroupMessage(String accessToken, String groupId,
			String mediaId) throws HttpException, IOException {
		Long rst = null;
		Map result = new HashMap();
		sendGroupMessage(accessToken, groupId, mediaId, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 群发组信息
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param message
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void sendGroupMessage(String accessToken, String groupId,
			String mediaId, Map result) throws HttpException, IOException {
		String url = SENDALL_MASS_API + "?access_token=" + accessToken;
		Map root = new LinkedHashMap();
		Map filter = new HashMap();
		filter.put("group_id", groupId);
		root.put("filter", filter);
		Map mpnews = new HashMap();
		mpnews.put("media_id", mediaId);
		root.put("mpnews", mpnews);
		root.put("msgtype", "mpnews");
		String content = objectMapper.writeValueAsString(root);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 发送用户群发信息
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param openIds
	 * @param mediaId
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static Long sendUsersMessage(String accessToken,
			List<String> openIds, String mediaId) throws HttpException,
			IOException {
		Long rst = null;
		Map result = new HashMap();
		sendUsersMessage(accessToken, openIds, mediaId, result);
		if (result.get("msg_id") != null)
			rst = Long.valueOf(result.get("msg_id").toString());
		return rst;
	}

	/**
	 * 发送用户群发信息
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param openIds
	 * @param mediaId
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void sendUsersMessage(String accessToken,
			List<String> openIds, String mediaId, Map result)
			throws HttpException, IOException {
		String url = USER_MASS_API + "?access_token=" + accessToken;
		Map root = new LinkedHashMap();
		root.put("touser", openIds);
		Map mpnews = new HashMap();
		mpnews.put("media_id", mediaId);
		root.put("mpnews", mpnews);
		root.put("msgtype", "mpnews");
		String content = objectMapper.writeValueAsString(root);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content,
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
	 * 预览
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param mediaId
	 * @param openId
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void previewMessage(String accessToken, String mediaId,
			String openId, Map<String, Object> result) throws HttpException,
			IOException {
		String url = PREVIEW_MASS_API + "?access_token=" + accessToken;
		Map<String, Object> jsonM1 = new LinkedHashMap<String, Object>();
		jsonM1.put("touser", openId);
		jsonM1.put("msgtype", "mpnews");
		Map<String, Object> jsonM2 = new LinkedHashMap<String, Object>();
		jsonM2.put("media_id", mediaId);
		jsonM1.put("mpnews", jsonM2);
		String jsonStr = objectMapper.writeValueAsString(jsonM1);
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
					Map<String, Object> json = objectMapper.readValue(response,
							Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 预览
	 * 
	 * @deprecated
	 * @param accessToken
	 * @param openId
	 * @param mediaId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static boolean previewMessage(String accessToken, String openId,
			String mediaId) throws JsonGenerationException,
			JsonMappingException, IOException {
		boolean rst = false;
		Map<String, Object> result = new HashMap<String, Object>();
		previewMessage(accessToken, openId, mediaId, result);
		if (result.get("errcode") != null)
			if (result.get("errcode").toString().equals("0")) {
				rst = true;
			} else if (!result.get("errcode").toString().equals("0")) {
				rst = false;
			}
		return rst;
	}

}
