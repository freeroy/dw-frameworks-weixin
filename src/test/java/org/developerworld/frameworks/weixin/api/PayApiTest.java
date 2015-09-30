package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.util.UUID;

import org.developerworld.frameworks.weixin.api.dto.PayJsSign;
import org.developerworld.frameworks.weixin.api.dto.PayOrderConfig;
import org.developerworld.frameworks.weixin.api.dto.PayUnifiedOrder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayApiTest {

	 
	private static PayOrderConfig payOrderConfig;
	private static PayUnifiedOrder unifiedOrder;
	private static String openId;

	//@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		payOrderConfig = new PayOrderConfig();
		payOrderConfig
				.setApiClientCert(new File("D:\\cert\\apiclient_cert.p12"));

		payOrderConfig.setApiKey("F382526931DFFF27E631C897394DBFA7");
		 
		payOrderConfig.setMchId("1230848902");
		payOrderConfig.setApiClientCertPassword("1230848902");
		// 属性参数
		String tradeNo = UUID.randomUUID().toString().replaceAll("-", "");
		payOrderConfig.setAppid("wxe3ce8d87e1b9b64b");
		unifiedOrder = new PayUnifiedOrder();
		unifiedOrder.setAttach("支付测试");
		unifiedOrder.setOutTradeNo(tradeNo);
		unifiedOrder.setBody("Ipad");
		unifiedOrder.setDetail("Ipad");
		unifiedOrder.setTotalFee(10);
		unifiedOrder.setSpbillCreateIp("14.23.103.66");
		unifiedOrder.setNotifyUrl("http://vanlead.pmparkchina.com/view/pay/callback.jsp");
		openId = "o4_J6t6ZYrXaT09zoEmxoq2f4N70";
		unifiedOrder.setOpenid(openId);
		unifiedOrder.setTradeType("JSAPI");
		
	
	}

	//@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	//@Test
	public void test() throws Exception {
		PayJsSign payJsSign=PayOrderApi.unifiedOrderJSAPI(payOrderConfig, unifiedOrder);
		Assert.assertNotNull(payJsSign);
	}

}
