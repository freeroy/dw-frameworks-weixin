package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.developerworld.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.api.dto.Template;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 模板API
 * 
 * @author Roy
 *
 */
public class TemplateApi {

	private static final String TEMPLATE_API = "https://api.weixin.qq.com/cgi-bin/message/template/send";

	private static final String GET_TEMPLATE_ID = "https://api.weixin.qq.com/cgi-bin/template/api_add_template";

	private static final String SET_TEMPLATE_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry";

	private static final String REQUEST_CONTENT_CHARSET = "UTF-8";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 设置模板行业
	 * 
	 * @param accessToken
	 * @param industryIds
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void changeIndustry(String accessToken, Set<Integer> industryIds) throws HttpException, IOException {
		Map result = new HashMap();
		changeIndustry(accessToken, industryIds, result);
	}

	/**
	 * 设置模板行业
	 * 
	 * @param accessToken
	 * @param industryIds
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void changeIndustry(String accessToken, Set<Integer> industryIds, Map result)
			throws HttpException, IOException {
		// 发送链接
		String url = SET_TEMPLATE_INDUSTRY + "?access_token=" + accessToken;
		// 构建提交的数据体
		Map<String, String> industryIdMap = new LinkedHashMap<String, String>();
		for (Integer industryId : industryIds)
			industryIdMap.put("industry_id" + (industryIdMap.size() + 1), industryId.toString());
		String content = objectMapper.writeValueAsString(industryIdMap);
		PostMethod post = new PostMethod(url);
		try {
			post.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content, "application/x-www-form-urlencoded",
					"utf-8");
			post.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String callback = post.getResponseBodyAsString();
				if (StringUtils.isNotBlank(callback)) {
					Map<String, Object> rstMap = objectMapper.readValue(callback, Map.class);
					if (result != null)
						result.putAll(rstMap);
				}
			}
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * 发送模板信息
	 * 
	 * @param accessToken
	 * @param template
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String sendTemplateMessage(String accessToken, Template template)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map result = new HashMap();
		sendTemplateMessage(accessToken, template, result);
		if (result.get("msgid") != null)
			return result.get("msgid").toString();
		return null;
	}

	/**
	 * 发送模板信息
	 * 
	 * @param accessToken
	 * @param template
	 * @param result
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void sendTemplateMessage(String accessToken, Template template, Map<String, Object> result)
			throws JsonGenerationException, JsonMappingException, IOException {
		// 发送链接
		String url = TEMPLATE_API + "?access_token=" + accessToken;
		String content = toJsonStr(template);
		PostMethod post = new PostMethod(url);
		try {
			post.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content, "application/x-www-form-urlencoded",
					"utf-8");
			post.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String tempRstJson = post.getResponseBodyAsString();
				Map<String, Object> rstMap = objectMapper.readValue(tempRstJson, Map.class);
				if (result != null)
					result.putAll(rstMap);
			}
		} finally {
			post.releaseConnection();
		}
	}

	private static String toJsonStr(Template template)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("touser", template.getTouser());
		data.put("template_id", template.getTemplateId());
		data.put("url", template.getUrl());
		data.put("topcolor", template.getTopcolor());
		data.put("data", template.getData());
		return objectMapper.writeValueAsString(data);
	}

	/**
	 * 获取模板Id
	 * 
	 * @param accessToken
	 * @param templateIdShort
	 * 
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String getTemplateId(String accessToken, String templateIdShort)
			throws JsonGenerationException, JsonMappingException, IOException {
		String templateId = null;
		Map<String, Object> result = new HashMap<String, Object>();
		getTemplateId(accessToken, templateIdShort, result);
		if (StringUtils.isNotBlank(result.get("errcode").toString()) && result.get("errcode").toString().equals("0")
				&& StringUtils.isNotBlank((String) result.get("errmsg")) && result.get("errmsg").equals("ok")) {
			templateId = (String) result.get("template_id");
		}
		return templateId;
	}

	/**
	 * 获取模板Id
	 * 
	 * @param accessToken
	 * @param templateIdShort
	 * @param result
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void getTemplateId(String accessToken, String templateIdShort, Map<String, Object> result)
			throws JsonGenerationException, JsonMappingException, IOException {
		// 发送链接
		String url = GET_TEMPLATE_ID + "?access_token=" + accessToken;
		String content = toJsonStr(templateIdShort);
		PostMethod post = new PostMethod(url);
		try {
			post.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(content, "application/x-www-form-urlencoded",
					"utf-8");
			post.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String rst = post.getResponseBodyAsString();
				Map<String, Object> rstMap = objectMapper.readValue(rst, Map.class);
				if (result != null)
					result.putAll(rstMap);
			}
		} finally {
			post.releaseConnection();
		}

	}

	/**
	 * 构建获取模板Id的json
	 * 
	 * @param templateIdShort
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static String toJsonStr(String templateIdShort)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("template_id_short", templateIdShort);
		return objectMapper.writeValueAsString(data);
	}
}
