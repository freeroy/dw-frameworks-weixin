package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.developerworld.frameworks.weixin.api.dto.CsRecord;
import org.developerworld.frameworks.weixin.api.dto.CustomerService;
import org.developerworld.frameworks.weixin.message.CsMessage;
import org.developerworld.frameworks.weixin.message.converter.CsMessageConverter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 客服信息API
 * 
 * @author Roy Huang
 * @version 20140307
 */
public class CsApi {

	private final static String SEND_MESSAGE_API = "https://api.weixin.qq.com/cgi-bin/message/custom/send";

	private final static String ADD_KF_ACCOUNT_API = "https://api.weixin.qq.com/customservice/kfaccount/add";

	private final static String UPDATE_KF_ACCOUNT_API = "https://api.weixin.qq.com/customservice/kfaccount/update";

	private final static String DELETE_KF_ACCOUNT_API = "https://api.weixin.qq.com/customservice/kfaccount/del";

	private final static String UPLOAD_KF_ACCOUNT_HEADIMG_API = "http://api.weixin.qq.com/customservice/kfaccount/uploadheadimg";

	private final static String GET_ONLINE_KF_LIST_API = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist";

	private final static String GET_KF_LIST_API = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist";
	
	//最新获取客服聊天记录接口链接
	private final static String GET_KF_RECORD_API = " https://api.weixin.qq.com/customservice/msgrecord/getrecord";
	//private final static String GET_KF_RECORD_API = "https://api.weixin.qq.com/cgi-bin/customservice/getrecord";
	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	private final static CsMessageConverter csMessageConverter = new CsMessageConverter();

	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 发送客服信息
	 * 
	 * @param accessToken
	 * @param csMessage
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void sendMessage(String accessToken, CsMessage csMessage)
			throws HttpException, IOException {
		Map result = new HashMap();
		sendMessage(accessToken,csMessage,result);
	}

	/**
	 * 发送客服信息
	 * 
	 * @param accessToken
	 * @param csMessage
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void sendMessage(String accessToken, CsMessage csMessage,Map result)
			throws HttpException, IOException {
		String url = SEND_MESSAGE_API + "?access_token=" + accessToken;
		String content = csMessageConverter.convertToJson(csMessage);
		PostMethod method = new PostMethod(url);
		try {
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
	 * 获取客服列表
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<CustomerService> getCustomerServices(String accessToken)
			throws HttpException, IOException {
		List<CustomerService> rst = new ArrayList<CustomerService>();
		Map result = new HashMap();
		getCustomerServices(accessToken, result);
		if (result.get("kf_list") != null) {
			List<Map<String, Object>> kfList = (List<Map<String, Object>>) result
					.get("kf_list");
			for (Map<String, Object> kf : kfList) {
				CustomerService cs = new CustomerService();
				cs.setKfAccount((String) kf.get("kf_account"));
				cs.setKfNick((String) kf.get("kf_nick"));
				cs.setKfId((String) kf.get("kf_id"));
				cs.setKfHeadimg((String) kf.get("kf_headimg"));
				rst.add(cs);
			}
		}
		return rst;
	}

	/**
	 * 获取客服列表
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getCustomerServices(String accessToken, Map result)
			throws HttpException, IOException {
		String url = GET_KF_LIST_API + "?access_token=" + accessToken;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
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
	 * 获取客服在线状态信息
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<CustomerService> getOnlineCustomerServices(
			String accessToken) throws HttpException, IOException {
		List<CustomerService> rst = new ArrayList<CustomerService>();
		Map result = new HashMap();
		getOnlineCustomerServices(accessToken, result);
		if (result.get("kf_online_list") != null) {
			List<Map<String, Object>> kfOnLines = (List<Map<String, Object>>) result
					.get("kf_online_list");
			if (kfOnLines != null) {
				for (Map<String, Object> kfOnLine : kfOnLines) {
					CustomerService cs = new CustomerService();
					cs.setKfAccount((String) kfOnLine.get("kf_account"));
					if (kfOnLine.get("status") != null)
						cs.setStatus(Byte.valueOf(kfOnLine.get("status")
								.toString()));
					cs.setKfId((String) kfOnLine.get("kf_id"));
					if (kfOnLine.get("auto_accept") != null)
						cs.setAutoAccept(Integer.valueOf(kfOnLine.get(
								"auto_accept").toString()));
					if (kfOnLine.get("accepted_case") != null)
						cs.setAcceptedCase(Integer.valueOf(kfOnLine.get(
								"accepted_case").toString()));
					rst.add(cs);
				}
			}
		}
		return rst;
	}

	/**
	 * 获取在线客服列表
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getOnlineCustomerServices(String accessToken, Map result)
			throws HttpException, IOException {
		String url = GET_ONLINE_KF_LIST_API + "?access_token=" + accessToken;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(
							response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 添加客服
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param nickname
	 * @param md5Password
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean addCustomerService(String accessToken,
			String KfAccount, String nickname, String md5Password)
			throws HttpException, IOException {
		Map result = new HashMap();
		addCustomerService(accessToken, KfAccount, nickname, md5Password,
				result);
		return result.get("errcode") != null
				&& result.get("errcode").toString().equals("0");
	}

	/**
	 * 添加客服
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param nickname
	 * @param md5Password
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void addCustomerService(String accessToken, String KfAccount,
			String nickname, String md5Password, Map result)
			throws HttpException, IOException {
		String url = ADD_KF_ACCOUNT_API + "?access_token=" + accessToken;
		Map<String, Object> jsonM1 = new LinkedHashMap<String, Object>();
		jsonM1.put("kf_account", KfAccount);
		jsonM1.put("nickname", nickname);
		jsonM1.put("password", md5Password);
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
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 更新客服信息
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param nickname
	 * @param md5Password
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean updateCustomerService(String accessToken,
			String KfAccount, String nickname, String md5Password)
			throws HttpException, IOException {
		Map result = new HashMap();
		updateCustomerService(accessToken, KfAccount, nickname, md5Password,
				result);
		return result.get("errcode") != null
				&& result.get("errcode").toString().equals("0");
	}

	/**
	 * 更新客服信息
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param nickname
	 * @param md5Password
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void updateCustomerService(String accessToken,
			String KfAccount, String nickname, String md5Password,
			Map result) throws HttpException, IOException {
		String url = UPDATE_KF_ACCOUNT_API + "?access_token=" + accessToken;
		Map<String, Object> jsonM1 = new LinkedHashMap<String, Object>();
		jsonM1.put("kf_account", KfAccount);
		jsonM1.put("nickname", nickname);
		jsonM1.put("password", md5Password);
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
					Map json =objectMapper.readValue(
							response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 上传客服头像
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param media
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean uploadCustomerServiceHeadImg(String accessToken,
			String KfAccount, File media) throws HttpException, IOException {
		Map result = new HashMap();
		uploadCustomerServiceHeadImg(accessToken, KfAccount, media, result);
		return result.get("errcode") != null
				&& result.get("errcode").toString().equals("0");
	}

	/**
	 * 上传客服头像
	 * 
	 * @param accessToken
	 * @param KfAccount
	 * @param media
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void uploadCustomerServiceHeadImg(String accessToken,
			String KfAccount, File media, Map result)
			throws HttpException, IOException {
		String url = UPLOAD_KF_ACCOUNT_HEADIMG_API + "?access_token="
				+ accessToken + "&kf_account=" + KfAccount;
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
					Map json = objectMapper.readValue(
							response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 删除客服S
	 * 
	 * @param accessToken
	 * @param openId
	 * @param mediaId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static boolean deleteCustomerService(String accessToken,
			String KfAccount) throws JsonGenerationException,
			JsonMappingException, IOException {
		Map result = new HashMap();
		deleteCustomerService(accessToken, KfAccount, result);
		return result.get("errcode") != null
				&& result.get("errcode").toString().equals("0");
	}

	/**
	 * 删除客服
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param openId
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void deleteCustomerService(String accessToken,
			String KfAccount, Map result) throws HttpException,
			IOException {
		String url = DELETE_KF_ACCOUNT_API + "?access_token=" + accessToken
				+ "&kf_account=" + KfAccount;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
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
	 * 获取客服聊天记录
	 * 
	 * @param accessToken
	 * @param starttime
	 * @param endtime
	 * @param openId
	 * @param pagesize
	 * @param pageindex
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<CsRecord> getCsRecord(String accessToken,
			Long starttime, Long endtime, String openId, int pagesize,
			int pageindex) throws HttpException, IOException {
		List<CsRecord> rst = new ArrayList<CsRecord>();
		Map result = new HashMap();
		getCsRecord(accessToken, starttime, endtime, openId, pagesize,
				pageindex, result);
		if (result.get("recordlist") != null) {
			List<Map<String, Object>> kfRecordList = (List<Map<String, Object>>) result
					.get("recordlist");
			for (Map<String, Object> kfRecord : kfRecordList) {
				CsRecord csr = new CsRecord();
				csr.setWorker((String) kfRecord.get("worker"));
				csr.setOpenid((String) kfRecord.get("openid"));
				if (kfRecord.get("opercode") != null)
					csr.setOpercode(Integer.valueOf(kfRecord.get("opercode")
							.toString()));
				if (kfRecord.get("time") != null)
					csr.setTime(Long.valueOf(kfRecord.get("time").toString()));
				csr.setText((String) kfRecord.get("text"));
				rst.add(csr);
			}
		}
		return rst;
	}

	/**
	 * 获取客服聊天记录
	 * 
	 * @param accessToken
	 * @param starttime
	 * @param endtime
	 * @param openId
	 * @param pagesize
	 * @param pageindex
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getCsRecord(String accessToken, Long starttime,
			Long endtime, String openId, int pagesize, int pageindex, Map result)
			throws HttpException, IOException {
		String url = GET_KF_RECORD_API + "?access_token=" + accessToken;
		Map<String, Object> jsonM1 = new LinkedHashMap<String, Object>();
		jsonM1.put("starttime", starttime);
		jsonM1.put("endtime", endtime);
		jsonM1.put("openid", openId);
		jsonM1.put("pagesize", pagesize);
		jsonM1.put("pageindex", pageindex);
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
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

}
