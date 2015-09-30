package org.developerworld.frameworks.weixin.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.developerworld.frameworks.weixin.api.dto.RedPack;
import org.developerworld.frameworks.weixin.api.dto.RedPackConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * 红包API
 * 
 * @author Roy Huang
 * @version 20150301
 * 
 */
public class RedPackApi {

	private final static String SEND_RED_PACK_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	/**
	 * 发送红包
	 * 
	 * @param redPackConfig
	 * @param redPack
	 * @param openId
	 * @return
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean sendRedPack(RedPackConfig redPackConfig,
			RedPack redPack, String openId) throws KeyManagementException,
			KeyStoreException, NoSuchAlgorithmException,
			UnrecoverableKeyException, CertificateException, IOException,
			DocumentException {
		Map result = new HashMap();
		sendRedPack(redPackConfig, redPack, openId, result);
		return result.get("result_code") != null
				&& result.get("result_code").equals("SUCCESS");
	}

	/**
	 * 发送红包
	 * 
	 * @param redPackConfig
	 * @param redPack
	 * @param openId
	 * @param result
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void sendRedPack(RedPackConfig redPackConfig,
			RedPack redPack, String openId, Map result)
			throws KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableKeyException,
			CertificateException, IOException, DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String mchBillno = buildBillNo(redPackConfig.getMchId(), new Date());
		String sign = buildSign(redPackConfig, redPack, openId, nonceStr,
				mchBillno);
		sendRedPack(redPackConfig, redPack, openId, nonceStr, mchBillno, sign,
				result);
	}

	/**
	 * 发送红包
	 * 
	 * @param redPackConfig
	 * @param redPack
	 * @param openId
	 * @param nonceStr
	 * @param mchBillno
	 * @param sign
	 * @param result
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws DocumentException
	 */
	public static void sendRedPack(RedPackConfig redPackConfig,
			RedPack redPack, String openId, String nonceStr, String mchBillno,
			String sign, Map result) throws IOException, KeyStoreException,
			KeyManagementException, NoSuchAlgorithmException,
			UnrecoverableKeyException, CertificateException, DocumentException {
		// 加载证书
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 获取apiClientCert
		InputStream apiClientCert = new ByteArrayInputStream(
				redPackConfig.getApiClientCert());
		try {
			keyStore.load(apiClientCert, redPackConfig
					.getApiClientCertPassword().toCharArray());
		} finally {
			apiClientCert.close();
		}
		// 构建ssl上下文及工厂
		SSLContext sslcontext = SSLContexts
				.custom()
				.loadKeyMaterial(keyStore,
						redPackConfig.getApiClientCertPassword().toCharArray())
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 构建SSL http客户端
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		try {
			// 创建协议
			HttpPost httpPost = new HttpPost(SEND_RED_PACK_API);
			// 构建传输内容
			String sendXml = buildSendXml(redPackConfig, redPack, openId,
					nonceStr, mchBillno, sign);
			// 设置类型
			StringEntity reqEntity = new StringEntity(sendXml,
					REQUEST_CONTENT_CHARSET);
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.setEntity(reqEntity);
			// 执行请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 获取反馈内容
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream in = entity.getContent();
					try {
						// 转换为字符串
						String callbackXml = IOUtils.toString(in,
								REQUEST_CONTENT_CHARSET);
						result.putAll(buildResultMap(callbackXml));
					} finally {
						in.close();
					}
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	/**
	 * 创建唯一订单号
	 * 
	 * @param mchId
	 * @param date
	 * @return
	 */
	public static String buildBillNo(String mchId, Date date) {
		return mchId + DATE_FORMAT.format(date)
				+ RandomStringUtils.randomNumeric(10);
	}

	/**
	 * 创建sign信息
	 * 
	 * @param redPackConfig
	 * @param redPack
	 * @param openId
	 * @param mchBillno
	 * @param nonceStr
	 * @return
	 */
	public static String buildSign(RedPackConfig redPackConfig,
			RedPack redPack, String openId, String nonceStr, String mchBillno) {
		StringBuilder stringA = new StringBuilder();
		stringA.append("act_name=" + redPack.getActName()).append(
				"&client_ip=" + redPackConfig.getClientIp());
		if (StringUtils.isNotBlank(redPackConfig.getLogoImgurl()))
			stringA.append("&logo_imgurl=" + redPackConfig.getLogoImgurl());
		stringA.append("&max_value=" + redPack.getMaxValue())
				.append("&mch_billno=" + mchBillno)
				.append("&mch_id=" + redPackConfig.getMchId())
				.append("&min_value=" + redPack.getMinValue())
				.append("&nick_name=" + redPackConfig.getNickName())
				.append("&nonce_str=" + nonceStr)
				.append("&re_openid=" + openId)
				.append("&remark=" + redPack.getRemark())
				.append("&send_name=" + redPackConfig.getSendName());
		if (StringUtils.isNotBlank(redPack.getShareContent()))
			stringA.append("&share_content=" + redPack.getShareContent());
		if (StringUtils.isNotBlank(redPack.getShareImgurl()))
			stringA.append("&share_imgurl=" + redPack.getShareImgurl());
		if (StringUtils.isNotBlank(redPack.getShareUrl()))
			stringA.append("&share_url=" + redPack.getShareUrl());
		if (StringUtils.isNotBlank(redPackConfig.getSubMchId()))
			stringA.append("&sub_mch_id=" + redPackConfig.getSubMchId());
		stringA.append("&total_amount=" + redPack.getTotalAmount())
				.append("&total_num=" + redPack.getTotalNum())
				.append("&wishing=" + redPack.getWishing())
				.append("&wxappid=" + redPackConfig.getWxappid());
		String stringSignTemp = stringA.toString() + "&key="
				+ redPackConfig.getApiKey();
		String sign = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
		return sign;
	}

	/**
	 * 构建反馈信息map
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildResultMap(String xmlStr) throws DocumentException {
		Map<String, Object> rst = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element e = doc.getRootElement();
		rst.put("return_code", e.elementText("return_code"));
		if (e.elements("return_msg") != null)
			rst.put("return_msg", e.elementText("return_msg"));
		if (e.elements("sign") != null)
			rst.put("sign", e.elementText("sign"));
		if (e.elements("result_code") != null)
			rst.put("result_code", e.elementText("result_code"));
		if (e.elements("err_code") != null)
			rst.put("err_code", e.elementText("err_code"));
		if (e.elements("err_code_des") != null)
			rst.put("err_code_des", e.elementText("err_code_des"));
		if (e.elements("mch_billno") != null)
			rst.put("mch_billno", e.elementText("mch_billno"));
		if (e.elements("mch_id") != null)
			rst.put("mch_id", e.elementText("mch_id"));
		if (e.elements("wxappid") != null)
			rst.put("wxappid", e.elementText("wxappid"));
		if (e.elements("re_openid") != null)
			rst.put("re_openid", e.elementText("re_openid"));
		if (e.elements("total_amount") != null)
			rst.put("total_amount",
					Integer.valueOf(e.elementText("total_amount")));
		return rst;
	}

	/**
	 * 构建用于发送的xml
	 * 
	 * @param redPackConfig
	 * @param redPack
	 * @param openId
	 * @param nonceStr
	 * @param mchBillno
	 * @param sign
	 * @return
	 */
	private static String buildSendXml(RedPackConfig redPackConfig,
			RedPack redPack, String openId, String nonceStr, String mchBillno,
			String sign) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>")
				.append("<sign>" + sign + "</sign>")
				.append("<mch_billno>" + mchBillno + "</mch_billno>")
				.append("<mch_id>" + redPackConfig.getMchId() + "</mch_id>")
				.append("<wxappid>" + redPackConfig.getWxappid() + "</wxappid>")
				.append("<nick_name>" + redPackConfig.getNickName()
						+ "</nick_name>")
				.append("<send_name>" + redPackConfig.getSendName()
						+ "</send_name>")
				.append("<re_openid>" + openId + "</re_openid>")
				.append("<total_amount>" + redPack.getTotalAmount()
						+ "</total_amount>")
				.append("<min_value>" + redPack.getMinValue() + "</min_value>")
				.append("<max_value>" + redPack.getMaxValue() + "</max_value>")
				.append("<total_num>" + redPack.getTotalNum() + "</total_num>")
				.append("<wishing>" + redPack.getWishing() + "</wishing>")
				.append("<client_ip>" + redPackConfig.getClientIp()
						+ "</client_ip>")
				.append("<act_name>" + redPack.getActName() + "</act_name>")
				.append("<remark>" + redPack.getRemark() + "</remark>")
				.append("<nonce_str>" + nonceStr + "</nonce_str>");
		if (StringUtils.isNotBlank(redPackConfig.getSubMchId()))
			sb.append("<sub_mch_id>" + redPackConfig.getSubMchId()
					+ "</sub_mch_id>");
		if (StringUtils.isNotBlank(redPackConfig.getLogoImgurl()))
			sb.append("<logo_imgurl>" + redPackConfig.getLogoImgurl()
					+ "</logo_imgurl>");
		if (StringUtils.isNotBlank(redPack.getShareContent()))
			sb.append("<share_content>" + redPack.getShareContent()
					+ "</share_content>");
		if (StringUtils.isNotBlank(redPack.getShareUrl()))
			sb.append("<share_url>" + redPack.getShareUrl() + "</share_url>");
		if (StringUtils.isNotBlank(redPack.getShareImgurl()))
			sb.append("<share_imgurl>" + redPack.getShareImgurl()
					+ "</share_imgurl>");
		sb.append("</xml>");
		return sb.toString();
	}
}
