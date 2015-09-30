package org.developerworld.frameworks.weixin.api;

import java.io.UnsupportedEncodingException;

import org.developerworld.frameworks.weixin.api.dto.WebAuthScope;
import org.junit.Assert;
import org.junit.Test;

public class WebAuthApiTest {

	//@Test
	public void testGetAuthUrl() throws UnsupportedEncodingException {
		String url=WebAuthApi.getAuthUrl(TestConfig.APP_ID, "http://www.developerworld.org", WebAuthScope.SNSAPI_USERINFO, "test");
		Assert.assertNotNull(url);
		System.out.println("get auth url:"+url);
	}

	//@Test
	public void testGetAccessTokenStringStringStringMap() {
		
	}

	//@Test
	public void testGetAccessTokenStringStringString() {
	}

	//@Test
	public void testRefreshAccessTokenStringStringMap() {
	}

	//@Test
	public void testRefreshAccessTokenStringString() {
	}

	//@Test
	public void testGetUserStringStringStringMap() {
	}

	//@Test
	public void testGetUserStringStringMap() {
	}

	//@Test
	public void testGetUserStringStringString() {
	}

	//@Test
	public void testGetUserStringString() {
	}

}
