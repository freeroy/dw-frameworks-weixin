package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.developerworld.frameworks.weixin.api.dto.MediaType;
import org.developerworld.frameworks.weixin.api.dto.NewsArticle;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 媒体API
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class MediaApi {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static String UPLOAD_MEDIA_API = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
	private final static String GET_MEDIA_API = "http://file.api.weixin.qq.com/cgi-bin/media/get";
	
	// private final static int UPLOAD_CONNECTION_TIMEOUT = 5000;

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	private final static Map<String, String> CONTENT_TYPE = new HashMap<String, String>();
	static {
		CONTENT_TYPE.put("jpg", "image/jpeg");
		CONTENT_TYPE.put("amr", "audio/amr");
		CONTENT_TYPE.put("mp3", "audio/mp3");
		CONTENT_TYPE.put("mp4", "video/mp4");
	}

	/**
	 * 上传图文素材
	 * 
	 * @deprecated see MassApi.uploadNews
	 * @param accessToken
	 * @param articles
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 */
	public static String uploadNews(String accessToken,
			List<NewsArticle> articles) throws JsonGenerationException,
			JsonMappingException, IOException {
		return MassApi.uploadNews(accessToken, articles);
	}

	/**
	 * 上传图文素材
	 * @deprecated see MassApi.uploadNews
	 * 
	 * @param accessToken
	 * @param articles
	 * @param result
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void uploadNews(String accessToken,
			List<NewsArticle> articles, Map result)
			throws JsonGenerationException, JsonMappingException, IOException {
		MassApi.uploadNews(accessToken, articles, result);
	}

	/**
	 * 上传媒体
	 * 
	 * @param accessToken
	 * @param type
	 * @param media
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String uploadMedia(String accessToken, MediaType type,
			File media) throws HttpException, IOException {
		Map result = new HashMap();
		uploadMedia(accessToken, type, media, result);
		if (result.get("type") != null) {
			if (result.get("media_id") != null)
				return (String) result.get("media_id");
			else if (result.get("thumb_media_id") != null)
				return (String) result.get("thumb_media_id");
		}
		return null;
	}

	public static void uploadMedia(String accessToken, MediaType type,
			File media, Map result) throws HttpException, IOException {
		String url = UPLOAD_MEDIA_API + "?access_token=" + accessToken
				+ "&type=" + type;
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			// 避免缓存
			method.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, false);
			// 添加附件
			method.setRequestEntity(new MultipartRequestEntity(
					new Part[] { new FilePart("media", media) }, method
							.getParams()));
			HttpClient client = new HttpClient();
			// 设置超时时间
			// client.getHttpConnectionManager().getParams()
			// .setConnectionTimeout(UPLOAD_CONNECTION_TIMEOUT);
			// 执行上传
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
	 * 获取媒体链接地址
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @return
	 */
	public static String getMediaUrl(String accessToken, String mediaId) {
		return GET_MEDIA_API + "?access_token=" + accessToken + "&media_id="
				+ mediaId;
	}

	/**
	 * 下载媒体
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @return
	 * @throws IOException
	 */
	public static byte[] getMedia(String accessToken, String mediaId)
			throws IOException {
		Map result = new HashMap();
		getMedia(accessToken, mediaId, result);
		if (result.get("media") != null)
			return (byte[]) result.get("media");
		return null;
	}

	/**
	 * 下载媒体
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param result
	 * @throws IOException
	 */
	public static void getMedia(String accessToken, String mediaId, Map result)
			throws IOException {
		String url = getMediaUrl(accessToken, mediaId);
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (method.getResponseContentLength() > 0) {
					byte[] data = method.getResponseBody();
					if (result != null)
						result.put("media", data);
				} else {
					if (result != null) {
						String response = method.getResponseBodyAsString();
						Map json = objectMapper.readValue(response, Map.class);
						result.putAll(json);
					}
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

}
