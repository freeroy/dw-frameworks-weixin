package org.developerworld.frameworks.weixin.handler.servlet;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.ResponseMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 接收信息响应servlet
 * 
 * @author Roy Huang
 * @version 20140306
 * 
 */
public abstract class AbstractSafeModeRequestMessageHandleServlet extends AbstractRequestMessageHandleServlet {

	private final static Log LOG = LogFactory.getLog(AbstractSafeModeRequestMessageHandleServlet.class);

	private final static String CHARSET = "utf-8";

	@Override
	protected RequestMessage buildRequestMessage(HttpServletRequest request, String token) {
		RequestMessage rst = null;
		try {
			// 加密类型
			String encryptType = request.getParameter("encrypt_type");
			// 判定是否为加密消息体
			if (StringUtils.isNotBlank(encryptType) && !encryptType.equals("raw")) {
				// 消息体的签名
				String msgSignature = request.getParameter("msg_signature");
				// 时间戳
				String timestamp = request.getParameter("timestamp");
				// 随机数
				String nonce = request.getParameter("nonce");
				// 获取微信提交过来的信息
				String requestMessageXml = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
				String newXml = decryptXml(request, token, msgSignature, timestamp, nonce, getAppId(request),
						requestMessageXml);
				if (StringUtils.isNotBlank(newXml))
					rst = requestMessageConverter.convertToObject(newXml);
			} else
				rst = super.buildRequestMessage(request, token);
		} catch (Exception e) {
			LOG.error(e);
		}
		return rst;
	}

	@Override
	protected String buildResponseMessageXML(HttpServletRequest request, String token,
			ResponseMessage responseMessage) {
		// 转化响应信息
		String rst = null;
		try {
			rst = super.buildResponseMessageXML(request, token, responseMessage);
			if (StringUtils.isNotBlank(rst)) {
				// 加密类型
				String encryptType = request.getParameter("encrypt_type");
				// 信息加密
				if (StringUtils.isNotBlank(encryptType) && !encryptType.equals("raw")) {
					// 时间戳
					String timestamp = request.getParameter("timestamp");
					// 随机数
					String nonce = request.getParameter("nonce");
					rst = encryXml(request, token, getAppId(request), timestamp, nonce, rst);
				}
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		return rst == null ? "" : rst;
	}

	/**
	 * 解密xml内容
	 * 
	 * @param request
	 * @param token
	 * @param msgSignature
	 * @param timestamp
	 * @param nonce
	 * @param appId
	 * @param xml
	 * @return
	 * @throws DocumentException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 */
	private String decryptXml(HttpServletRequest request, String token, String msgSignature, String timestamp,
			String nonce, String appId, String xml)
					throws DocumentException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
					NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
					InvalidAlgorithmParameterException, UnsupportedEncodingException {
		String msgEncrypt = getXmlMsgEncrypt(xml);
		if (StringUtils.isBlank(msgEncrypt))
			return null;
		// 构建验证数组
		String[] msgSignatureArray = new String[] { token, timestamp, nonce, msgEncrypt };
		// 字典排序
		Arrays.sort(msgSignatureArray);
		// 构建验证字符串
		String _msgSignature = "";
		for (String msa : msgSignatureArray)
			_msgSignature += msa;
		// sha1加密
		_msgSignature = DigestUtils.sha1Hex(_msgSignature);
		// 校验消息签名
		if (!_msgSignature.equals(msgSignature))
			return null;
		String encodingAesKey = getEncodingAESKey(request);
		byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");
		// 对消息进行解密
		byte[] aesMsg = Base64.decodeBase64(msgEncrypt);
		byte[] ranMsg = decrypt(aesKey, aesMsg);
		// 去除补位字符
		byte[] bytes = encoderPKCS7(ranMsg);
		// 分离16位随机字符串,网络字节序和AppId
		byte[] networkOrder = ArrayUtils.subarray(bytes, 16, 20);
		int xmlLength = recoverNetworkBytesOrder(networkOrder);
		// 获取appId
		String _appId = new String(ArrayUtils.subarray(bytes, 20 + xmlLength, bytes.length), CHARSET);
		if (!appId.equals(_appId))
			return null;
		// 获取原文内容
		return new String(ArrayUtils.subarray(bytes, 20, 20 + xmlLength), CHARSET);
	}

	/**
	 * 根据模式号进行加密或解密操作
	 * 
	 * @param key
	 * @param data
	 * @param mode
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 */
	private byte[] decrypt(byte key[], byte data[])
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] original;
		// 设置解密模式为AES的CBC模式
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec key_spec = new SecretKeySpec(key, "AES");
		IvParameterSpec iv = new IvParameterSpec(ArrayUtils.subarray(key, 0, 16));
		cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);
		// 解密
		original = cipher.doFinal(data);
		return original;

	}

	/**
	 * 还原4个字节的网络字节序
	 * 
	 * @param orderBytes
	 * @return
	 */
	private int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	/**
	 * 获取xml内容中的密文
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	private String getXmlMsgEncrypt(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element e = doc.getRootElement();
		return e.elementText("Encrypt");
	}

	/**
	 * pkcs7编码
	 * 
	 * @param data
	 * @return
	 */
	private byte[] encoderPKCS7(byte[] data) {
		int pad = (int) data[data.length - 1];
		if (pad < 1 || pad > 32)
			pad = 0;
		return ArrayUtils.subarray(data, 0, data.length - pad);
	}

	/**
	 * 加密xml
	 * 
	 * @param request
	 * @param token
	 * @param appId
	 * @param timestamp
	 * @param nonce
	 * @param outXml
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 */
	private String encryXml(HttpServletRequest request, String token, String appId, String timestamp, String nonce,
			String outXml) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
					InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
					InvalidAlgorithmParameterException {
		String encodingAesKey = getEncodingAESKey(request);
		byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");
		// 16位随机字符串
		String random16Str = RandomStringUtils.randomAlphanumeric(16);
		byte[] random16StrByte = random16Str.getBytes(CHARSET);
		byte[] outXmlByte = outXml.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(outXmlByte.length);
		byte[] appIdBytes = appId.getBytes(CHARSET);
		// randomStr + networkBytesOrder + text + appid
		byte[] msgEncryptByte = ArrayUtils.addAll(ArrayUtils.addAll(random16StrByte, networkBytesOrder),
				ArrayUtils.addAll(outXmlByte, appIdBytes));
		// 添加补位
		msgEncryptByte = ArrayUtils.addAll(msgEncryptByte, encoderPKCS7(msgEncryptByte.length));
		// 进行加密
		String msgEncrypt = encrypt(aesKey, msgEncryptByte);
		// 构建验证数组
		String[] msgSignatureArray = new String[] { token, timestamp, nonce, msgEncrypt };
		// 字典排序
		Arrays.sort(msgSignatureArray);
		// 构建验证字符串
		String _msgSignature = "";
		for (String msa : msgSignatureArray)
			_msgSignature += msa;
		// sha1加密
		_msgSignature = DigestUtils.sha1Hex(_msgSignature);
		// 构建返回xml
		StringBuilder _outXml = new StringBuilder().append("<xml>");
		_outXml.append("<Encrypt><![CDATA[").append(msgEncrypt).append("]]></Encrypt>");
		_outXml.append("<MsgSignature><![CDATA[").append(_msgSignature).append("]]></MsgSignature>");
		_outXml.append("<TimeStamp>").append(timestamp).append("</TimeStamp>");
		_outXml.append("<Nonce><![CDATA[").append(nonce).append("]]></Nonce>");
		_outXml.append("</xml>");
		return _outXml.toString();
	}

	/**
	 * 计算需要填充的位数
	 * 
	 * @param count
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private byte[] encoderPKCS7(int count) throws UnsupportedEncodingException {
		// 计算需要填充的位数
		int amountToPad = 32 - (count % 32);
		if (amountToPad == 0)
			amountToPad = 32;
		// 获得补位所用的字符
		char padChr = (char) ((byte) (amountToPad & 0xFF));
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++)
			tmp += padChr;
		return tmp.getBytes(CHARSET);
	}

	/**
	 * 加密
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 */
	private String encrypt(byte key[], byte data[])
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		try {
			Base64 base64 = new Base64();
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			// 加密
			byte[] encrypted = cipher.doFinal(data);
			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = base64.encodeToString(encrypted);
			return base64Encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成4个字节的网络字节序
	 * 
	 * @param sourceNumber
	 * @return
	 */
	private byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	/**
	 * 根据请求信息，获取AES钥匙信息
	 * 
	 * @param request
	 * @return
	 */
	protected abstract String getEncodingAESKey(HttpServletRequest request);

	/**
	 * 获取appId信息
	 * 
	 * @param request
	 * @return
	 */
	protected abstract String getAppId(HttpServletRequest request);

}
