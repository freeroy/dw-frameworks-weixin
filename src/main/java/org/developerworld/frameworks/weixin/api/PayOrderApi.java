package org.developerworld.frameworks.weixin.api;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.developerworld.commons.lang.MapBuilder;
import org.developerworld.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.api.dto.PayJsSign;
import org.developerworld.frameworks.weixin.api.dto.PayOrderConfig;
import org.developerworld.frameworks.weixin.api.dto.PayRefund;
import org.developerworld.frameworks.weixin.api.dto.PayUnifiedOrder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 支付api
 * 
 * @author Roy Huang
 * @version 20150505
 * 
 */
public class PayOrderApi extends AbstractBaseApi {

	private final static String UNIFIED_ORDER_QUERY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private final static String ORDER_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
	private final static String CLOSE_ORDER_API = "https://api.mch.weixin.qq.com/pay/closeorder";
	private final static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	private final static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	private final static String REQUEST_CONTENT_CHARSET = "UTF-8";

	/**
	 * 统一下单
	 * 
	 * @param payOrderConfig
	 * @param payUnifiedOrder
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void unifiedOrder(PayOrderConfig payOrderConfig,
			PayUnifiedOrder payUnifiedOrder, Map result)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, IOException, DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String sign = buildSign(payOrderConfig, nonceStr, payUnifiedOrder);
		unifiedOrder(payOrderConfig, payUnifiedOrder, nonceStr, sign, result);
	}

	/**
	 * 获取js签名
	 */
	public static PayJsSign unifiedOrderJSAPI(PayOrderConfig payOrderConfig,
			PayUnifiedOrder unifiedOrder) throws Exception {
		PayJsSign rst = null;
		unifiedOrder.setTradeType(PayUnifiedOrder.TRADE_TYPE_JSAPI);
		Map<String, Object> result = new HashMap<String, Object>();
		unifiedOrder(payOrderConfig, unifiedOrder, result);
		if ("SUCCESS".equals(result.get("return_code"))
				&& "SUCCESS".equals(result.get("result_code"))) {
			rst = new PayJsSign();
			rst.setAppId((String) result.get("appid"));
			rst.setTimeStamp(new Date().getTime());
			rst.setNonceStr((String) result.get("nonce_str"));
			rst.setPackage("prepay_id=" + result.get("prepay_id"));
			rst.setSignType("MD5");
			rst.setPaySign(buildJsPaySign(rst.getAppId(), rst.getTimeStamp(),
					rst.getNonceStr(), rst.getPackage(), rst.getSignType(),
					payOrderConfig.getApiKey()));

		}
		return rst;
	}

	/**
	 * 构建js签名
	 * 
	 * @param appId
	 * @param timeStamp
	 * @param nonceStr
	 * @param _package
	 * @param signType
	 * @param apiKey
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static String buildJsPaySign(String appId, Long timeStamp,
			String nonceStr, String _package, String signType, String apiKey)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		TreeMap<String, Object> tMap = buildObjectTreeMap(
				new MapBuilder().put("appId", appId).put("nonceStr", nonceStr)
						.put("package", _package).put("timeStamp", timeStamp)
						.put("signType", signType).map(), false);
		StringBuilder stringA = new StringBuilder();
		// 获取设置参数值
		Iterator<Entry<String, Object>> iterator = tMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			if (entry.getValue() != null
					&& StringUtils.isNotBlank(entry.getValue().toString()))
				stringA.append(entry.getKey()).append("=")
						.append(entry.getValue()).append("&");
		}
		stringA.append("key=").append(apiKey);
		return DigestUtils.md5Hex(stringA.toString()).toUpperCase();
	}

	/**
	 * 统一下单
	 * 
	 * @param payOrderConfig
	 * @param payUnifiedOrder
	 * @param nonceStr
	 * @param sign
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void unifiedOrder(PayOrderConfig payOrderConfig,
			PayUnifiedOrder payUnifiedOrder, String nonceStr, String sign,
			Map result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, IOException,
			DocumentException {
		String url = UNIFIED_ORDER_QUERY_API;
		String requestXml = buildUnifiedOrderRequestXml(payOrderConfig,
				payUnifiedOrder, nonceStr, sign);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(requestXml,
					"application/x-www-form-urlencoded", "utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map data = buildUnifiedOrderResponseMap(response);
					result.putAll(data);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 创建请求xml
	 * 
	 * @param payOrderConfig
	 * @param payUnifiedOrder
	 * @param nonceStr
	 * @param sign
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static String buildUnifiedOrderRequestXml(
			PayOrderConfig payOrderConfig, PayUnifiedOrder payUnifiedOrder,
			String nonceStr, String sign) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		rst.append("<xml>");
		rst.append("<appid>").append(payOrderConfig.getAppid())
				.append("</appid>");
		rst.append("<mch_id>").append(payOrderConfig.getMchId())
				.append("</mch_id>");
		if(payOrderConfig.getDeviceInfo()!=null)
			rst.append("<device_info>").append(payOrderConfig.getDeviceInfo()).append("</device_info>");
		rst.append(objectToXmlStr(payUnifiedOrder));
		rst.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
		rst.append("<sign>").append(sign).append("</sign>");
		rst.append("</xml>");
		return rst.toString();
	}

	/**
	 * 构建统一下单响应map
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildUnifiedOrderResponseMap(String xmlStr)
			throws DocumentException {
		Map<String, Object> rst = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element e = doc.getRootElement();
		List<Element> ces = e.elements();
		for (Element ce : ces)
			rst.put(ce.getName(), ce.getTextTrim());
		return rst;
	}

	/**
	 * 查询订单
	 * 
	 * @param payOrderConfig
	 * @param transactionId
	 * @param outTradeNo
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws HttpException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void orderQuery(PayOrderConfig payOrderConfig,
			String transactionId, String outTradeNo, Map result)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, HttpException, IOException,
			DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String sign = buildSign(
				payOrderConfig,
				nonceStr,
				new MapBuilder().put("transactionId", transactionId)
						.put("outTradeNo", outTradeNo).map());
		orderQuery(payOrderConfig, transactionId, outTradeNo, nonceStr, sign,
				result);
	}

	/**
	 * 查询订单
	 * 
	 * @param payOrderConfig
	 * @param transactionId
	 * @param outTradeNo
	 * @param nonceStr
	 * @param sign
	 * @param result
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws HttpException
	 * @throws DocumentException
	 */
	public static void orderQuery(PayOrderConfig payOrderConfig,
			String transactionId, String outTradeNo, String nonceStr,
			String sign, Map result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, HttpException,
			IOException, DocumentException {
		String url = ORDER_QUERY_API;
		String requestXml = buildOrderQueryRequestXml(payOrderConfig,
				transactionId, outTradeNo, nonceStr, sign);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(requestXml,
					"application/x-www-form-urlencoded", "utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map data = buildOrderQueryResponseMap(response);
					result.putAll(data);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 构建查询订单请求xml
	 * 
	 * @param payOrderConfig
	 * @param transactionId
	 * @param outTradeNo
	 * @param nonceStr
	 * @param sign
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static String buildOrderQueryRequestXml(
			PayOrderConfig payOrderConfig, String transactionId,
			String outTradeNo, String nonceStr, String sign)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		rst.append("<xml>");
		rst.append(objectToXmlStr(payOrderConfig));
		if (StringUtils.isNotBlank(transactionId))
			rst.append("<transaction_id>").append(transactionId)
					.append("</transaction_id>");
		if (StringUtils.isNotBlank(outTradeNo))
			rst.append("<out_trade_no>").append(outTradeNo)
					.append("</out_trade_no>");
		rst.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
		rst.append("<sign>").append(sign).append("</sign>");
		rst.append("</xml>");
		return rst.toString();
	}

	/**
	 * 构建查询订单响应map
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildOrderQueryResponseMap(String xmlStr)
			throws DocumentException {
		Map<String, Object> rst = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element e = doc.getRootElement();
		List<Element> ces = e.elements();
		for (Element ce : ces) {
			String name = ce.getName();
			String value = ce.getTextTrim();
			if (StringUtils.isNotBlank(value)) {
				if (name.equals("total_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("cash_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("coupon_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("coupon_count"))
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("coupon_fee_") == 0)
					rst.put(name, Integer.parseInt(value));
				else
					rst.put(name, value);
			} else
				rst.put(name, value);
		}
		return rst;
	}

	/**
	 * 关闭订单
	 * 
	 * @param payOrderConfig
	 * @param outTradeNo
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws HttpException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void closeOrder(PayOrderConfig payOrderConfig,
			String outTradeNo, Map result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, HttpException,
			IOException, DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String sign = buildSign(payOrderConfig, nonceStr,
				new MapBuilder().put("outTradeNo", outTradeNo).map());
		closeOrder(payOrderConfig, outTradeNo, nonceStr, sign, result);
	}

	/**
	 * 关闭订单
	 * 
	 * @param payOrderConfig
	 * @param outTradeNo
	 * @param nonceStr
	 * @param sign
	 * @param result
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws HttpException
	 * @throws DocumentException
	 */
	public static void closeOrder(PayOrderConfig payOrderConfig,
			String outTradeNo, String nonceStr, String sign, Map result)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, HttpException, IOException,
			DocumentException {
		String url = CLOSE_ORDER_API;
		String requestXml = buildCloseOrderRequestXml(payOrderConfig,
				outTradeNo, nonceStr, sign);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(requestXml,
					"application/x-www-form-urlencoded", "utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map data = buildCloseOrderResponseMap(response);
					result.putAll(data);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 创建关闭订单请求xml
	 * 
	 * @param payOrderConfig
	 * @param outTradeNo
	 * @param nonceStr
	 * @param sign
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private static String buildCloseOrderRequestXml(
			PayOrderConfig payOrderConfig, String outTradeNo, String nonceStr,
			String sign) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		rst.append("<xml>");
		rst.append(objectToXmlStr(payOrderConfig));
		if (StringUtils.isNotBlank(outTradeNo))
			rst.append("<out_trade_no>").append(outTradeNo)
					.append("</out_trade_no>");
		rst.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
		rst.append("<sign>").append(sign).append("</sign>");
		rst.append("</xml>");
		return rst.toString();
	}

	/**
	 * 创建关闭订单响应对象
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildCloseOrderResponseMap(String xmlStr)
			throws DocumentException {
		return buildUnifiedOrderResponseMap(xmlStr);
	}

	/**
	 * 申请退款
	 */
	public static void refund(PayOrderConfig payOrderConfig,
			PayRefund payRefund, Map result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, IOException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			KeyManagementException, UnrecoverableKeyException,
			DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String sign = buildSign(payOrderConfig, nonceStr, payRefund);
		refund(payOrderConfig, payRefund, nonceStr, sign, result);
	}

	/**
	 * 申请退款
	 * 
	 * @param payOrderConfig
	 * @param payRefund
	 * @param nonceStr
	 * @param sign
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws DocumentException
	 */
	public static void refund(PayOrderConfig payOrderConfig,
			PayRefund payRefund, String nonceStr, String sign, Map result)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, IOException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			KeyManagementException, UnrecoverableKeyException,
			DocumentException {
		// 加载证书
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 获取apiClientCert
		InputStream apiClientCert = new ByteArrayInputStream(
				payOrderConfig.getApiClientCert());
		try {
			keyStore.load(apiClientCert, payOrderConfig
					.getApiClientCertPassword().toCharArray());
		} finally {
			apiClientCert.close();
		}
		// 构建ssl上下文及工厂
		SSLContext sslcontext = SSLContexts
				.custom()
				.loadKeyMaterial(keyStore,
						payOrderConfig.getApiClientCertPassword().toCharArray())
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 构建SSL http客户端
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		try {
			// 创建协议
			HttpPost httpPost = new HttpPost(REFUND_API);
			// 构建传输内容
			String sendXml = buildRefundRequestXml(payOrderConfig, payRefund,
					nonceStr, sign);
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
						result.putAll(buildRefundResponseMap(callbackXml));
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
	 * 构建申请退款请求xml
	 * 
	 * @param payOrderConfig
	 * @param payRefund
	 * @param nonceStr
	 * @param sign
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static String buildRefundRequestXml(PayOrderConfig payOrderConfig,
			PayRefund payRefund, String nonceStr, String sign)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		rst.append("<xml>");
		rst.append("<appid>").append(payOrderConfig.getAppid())
				.append("</appid>");
		rst.append("<mch_id>").append(payOrderConfig.getMchId())
				.append("</mch_id>");
		if(payOrderConfig.getDeviceInfo()!=null)
			rst.append("<device_info>").append(payOrderConfig.getDeviceInfo()).append("</device_info>");
		rst.append(objectToXmlStr(payRefund));
		rst.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
		rst.append("<sign>").append(sign).append("</sign>");
		rst.append("</xml>");
		return rst.toString();
	}

	/**
	 * 构建申请退款响应信息
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildRefundResponseMap(String xmlStr)
			throws DocumentException {
		Map<String, Object> rst = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element e = doc.getRootElement();
		List<Element> ces = e.elements();
		for (Element ce : ces) {
			String name = ce.getName();
			String value = ce.getTextTrim();
			if (StringUtils.isNotBlank(value)) {
				if (name.equals("refund_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("total_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("cash_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("cash_refund_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("coupon_refund_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("coupon_refund_count"))
					rst.put(name, Integer.parseInt(value));
				else
					rst.put(name, value);
			} else
				rst.put(name, value);
		}
		return rst;
	}

	/**
	 * 查询退款
	 */
	public static void refundQuery(PayOrderConfig payOrderConfig,
			String transactionId, String outTradeNo, String outRefundNo,
			String refundId, Map result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, HttpException,
			IOException, DocumentException {
		// 构建其它属性
		String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
		String sign = buildSign(
				payOrderConfig,
				nonceStr,
				new MapBuilder().put("transactionId", transactionId)
						.put("outTradeNo", outTradeNo)
						.put("outRefundNo", outRefundNo)
						.put("refundId", refundId).map());
		refundQuery(payOrderConfig, transactionId, outTradeNo, outRefundNo,
				refundId, nonceStr, sign, result);
	}

	/**
	 * 查询退款
	 * 
	 * @param payOrderConfig
	 * @param transactionId
	 * @param outTradeNo
	 * @param outRefundNo
	 * @param refundId
	 * @param nonceStr
	 * @param sign
	 * @param result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws HttpException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void refundQuery(PayOrderConfig payOrderConfig,
			String transactionId, String outTradeNo, String outRefundNo,
			String refundId, String nonceStr, String sign, Map result)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, HttpException, IOException,
			DocumentException {
		String url = REFUND_QUERY_API;
		String requestXml = buildRefundQueryRequestXml(payOrderConfig,
				transactionId, outTradeNo, outRefundNo, refundId, nonceStr,
				sign);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(requestXml,
					"application/x-www-form-urlencoded", "utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map data = buildRefundQueryResponseMap(response);
					result.putAll(data);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 构造查询退款请求xml
	 * 
	 * @param payOrderConfig
	 * @param transactionId
	 * @param outTradeNo
	 * @param outRefundNo
	 * @param refundId
	 * @param nonceStr
	 * @param sign
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static String buildRefundQueryRequestXml(
			PayOrderConfig payOrderConfig, String transactionId,
			String outTradeNo, String outRefundNo, String refundId,
			String nonceStr, String sign) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		rst.append("<xml>");
		rst.append(objectToXmlStr(payOrderConfig));
		if (StringUtils.isNotBlank(transactionId))
			rst.append("<transaction_id>").append(transactionId)
					.append("</transaction_id>");
		if (StringUtils.isNotBlank(transactionId))
			rst.append("<out_trade_no>").append(outTradeNo)
					.append("</out_trade_no>");
		if (StringUtils.isNotBlank(outRefundNo))
			rst.append("<out_refund_no>").append(outRefundNo)
					.append("</out_refund_no>");
		if (StringUtils.isNotBlank(refundId))
			rst.append("<refund_id>").append(refundId).append("</refund_id>");
		rst.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
		rst.append("<sign>").append(sign).append("</sign>");
		rst.append("</xml>");
		return rst.toString();
	}

	/**
	 * 构建产寻退款响应信息
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	private static Map buildRefundQueryResponseMap(String xmlStr)
			throws DocumentException {
		Map<String, Object> rst = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element e = doc.getRootElement();
		List<Element> ces = e.elements();
		for (Element ce : ces) {
			String name = ce.getName();
			String value = ce.getTextTrim();
			if (StringUtils.isNotBlank(value)) {
				if (name.equals("total_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("cash_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("refund_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("coupon_refund_fee"))
					rst.put(name, Integer.parseInt(value));
				else if (name.equals("refund_count"))
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("refund_fee_") == 0)
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("fee_type_") == 0)
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("coupon_refund_fee_") == 0)
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("coupon_refund_count_") == 0)
					rst.put(name, Integer.parseInt(value));
				else if (name.indexOf("coupon_refund_fee_") == 0)
					rst.put(name, Integer.parseInt(value));
				else
					rst.put(name, value);
			} else
				rst.put(name, value);
		}
		return rst;
	}

	/**
	 * 根据对象，返回treeMap对象
	 * 
	 * @param object
	 * @param camelToUnderling
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static TreeMap<String, Object> buildObjectTreeMap(Object object,
			boolean camelToUnderling) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		TreeMap<String, Object> rst = new TreeMap<String, Object>();
		if (object != null) {
			if (object instanceof Map) {
				Iterator<Entry> iterator = ((Map) object).entrySet().iterator();
				while (iterator.hasNext()) {
					Entry entry = iterator.next();
					String key = (String) entry.getKey();
					if (camelToUnderling)
						key = StringUtils.camelToUnderling(key);
					rst.put(key, entry.getValue());
				}
			} else {
				PropertyDescriptor[] pros = PropertyUtils
						.getPropertyDescriptors(object);
				for (int i = 0; i < pros.length; i++) {
					PropertyDescriptor pro = pros[i];
					String name = pro.getName();
					if ("class".equals(name))
						continue; // No point in trying to set an object's class
					String value = BeanUtils.getProperty(object, name);
					if (camelToUnderling)
						name = StringUtils.camelToUnderling(name);
					rst.put(name, value);
				}
			}
		}
		return rst;
	}

	/**
	 * 构建签名字符串
	 * 
	 * @param payOrderConfig
	 * @param nonceStr
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static String buildSign(PayOrderConfig payOrderConfig,
			String nonceStr, Object... args) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		StringBuilder stringA = new StringBuilder();
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		// 获取设置参数值
		if (args != null) {
			for (Object arg : args)
				params.putAll(buildObjectTreeMap(arg, true));
		}
		// 设置其它参数
		params.putAll(buildObjectTreeMap(payOrderConfig, true));
		params.put("nonce_str", nonceStr);
		// 删除不能出现的参数
		params.remove("api_client_cert");
		params.remove("api_client_cert_password");
		params.remove("api_key");
		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			if (entry.getValue() != null
					&& StringUtils.isNotBlank(entry.getValue().toString()))
				stringA.append(entry.getKey()).append("=")
						.append(entry.getValue()).append("&");
		}
		stringA.append("key=").append(payOrderConfig.getApiKey());
		return DigestUtils.md5Hex(stringA.toString()).toUpperCase();
	}
}
