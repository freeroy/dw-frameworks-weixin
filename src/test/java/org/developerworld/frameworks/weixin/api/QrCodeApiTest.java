package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class QrCodeApiTest {

	private static String accessToken;

	//@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
	}

	//@AfterClass
	public static void after() {
		accessToken = null;
	}

	//@Test
	public void testCreateTempQrCodeStringIntegerLongMap()
			throws JsonGenerationException, JsonMappingException, IOException {
		Map result = new HashMap();
		QrCodeApi.createTempQrCode(accessToken, null, RandomUtils.nextInt(99999)+1, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("create temp QR code result:" + result);
	}

	//@Test
	public void testCreateTempQrCodeStringLongMap()
			throws JsonGenerationException, JsonMappingException, IOException {
		Map result = new HashMap();
		QrCodeApi.createTempQrCode(accessToken, RandomUtils.nextInt(99999)+1, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("create temp QR code result:" + result);
	}

	//@Test
	public void testCreateTempQrCodeStringIntegerLong()
			throws JsonGenerationException, JsonMappingException, IOException {
		String ticket = QrCodeApi.createTempQrCode(accessToken, null, RandomUtils.nextInt(99999)+1);
		System.out.println("create temp QR code:" + ticket);
	}

	//@Test
	public void testCreateTempQrCodeStringLong()
			throws JsonGenerationException, JsonMappingException, IOException {
		String ticket = QrCodeApi.createTempQrCode(accessToken, RandomUtils.nextInt(99999)+1);
		System.out.println("create temp QR code:" + ticket);
	}

	//@Test
	public void testCreateQrCodeStringLongMap() throws JsonGenerationException,
			JsonMappingException, IOException {
		Map result = new HashMap();
		QrCodeApi.createQrCode(accessToken, RandomUtils.nextInt(99999)+1, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("create QR code result:" + result);
	}

	//@Test
	public void testCreateQrCodeStringLong() throws JsonGenerationException,
			JsonMappingException, IOException {
		String ticket = QrCodeApi.createQrCode(accessToken, RandomUtils.nextInt(99999)+1);
		System.out.println("create QR code:" + ticket);
	}

	//@Test
	public void testGetQrCodeUrl() throws JsonGenerationException,
			JsonMappingException, IOException {
		String ticket = QrCodeApi.createQrCode(accessToken, RandomUtils.nextInt(99999)+1);
		if (ticket != null) {
			String url = QrCodeApi.getQrCodeUrl(ticket);
			System.out.println("get QR code url:" + url);
		}
	}

	//@Test
	public void testGetQrCode() throws IOException {
		String ticket = QrCodeApi.createQrCode(accessToken, RandomUtils.nextInt(99999)+1);
		if (ticket != null) {
			byte[] datas = QrCodeApi.getQrCode(ticket);
			System.out.println("get QR code:" + datas);

		}
	}

}
