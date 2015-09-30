package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class AccessTokenApiTest {

	//@Test
	public void testGetAccessTokenStringString() throws IOException {
		String rst = AccessTokenApi.getAccessToken(TestConfig.APP_ID, TestConfig.SECRET);
		System.out.println("accessToken:"+rst);
	}

	//@Test
	public void testGetAccessTokenStringStringMap() throws IOException {
		Map result = new HashMap();
		AccessTokenApi.getAccessToken(TestConfig.APP_ID, TestConfig.SECRET, result);
		Assert.assertTrue(result.size()>0);
		System.out.println("accessToken result:"+result);
	}

}
