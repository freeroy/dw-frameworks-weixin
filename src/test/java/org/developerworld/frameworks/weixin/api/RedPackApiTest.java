package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.developerworld.frameworks.weixin.api.dto.RedPack;
import org.developerworld.frameworks.weixin.api.dto.RedPackConfig;
import org.dom4j.DocumentException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RedPackApiTest {

	private static RedPackConfig redPackConfig;
	private static RedPack redPack;
	private static String openId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		redPackConfig = new RedPackConfig();
		redPackConfig
				.setApiClientCert(new File("D:\\cert\\apiclient_cert.p12"));
		// redPackConfig.setApiClientCertPassword("");
		redPackConfig.setApiKey("4eba32c5cb4252cf43847887f9887023");
		redPackConfig.setClientIp("14.23.103.66");
		// redPackConfig.setLogoImgurl("");
		redPackConfig.setMchId("12311091 02");
		redPackConfig.setNickName("立白集团");
		redPackConfig.setSendName("快乐家");
		// redPackConfig.setSubMchId("");
		redPackConfig.setWxappid("wx26be12d215d3d8cb");

		redPack = new RedPack();
		redPack.setActName("微信红包");
		redPack.setMaxValue(100);
		redPack.setMinValue(100);
		redPack.setRemark("接口测试");
		// redPack.setShareContent("");
		// redPack.setShareImgurl("");
		// redPack.setShareUrl("");
		redPack.setTotalAmount(100);
		redPack.setTotalNum(1);
		redPack.setWishing("希望成功");

		openId = "oPOant0jGECn1D6rHYnZsVOxKuKo";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	//@Test
	public void test() throws KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableKeyException,
			CertificateException, IOException, DocumentException {
		boolean rst = RedPackApi.sendRedPack(redPackConfig, redPack, openId);
		Assert.assertTrue(rst);
	}

}
