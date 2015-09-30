package org.developerworld.frameworks.weixin.api;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.developerworld.frameworks.weixin.api.dto.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserApiTest {

	private static String accessToken;
	private static String openId;

	//@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		List<String> users = UserApi.getUsers(accessToken);
		openId = users.size() > 0 ? users.get(0) : null;
	}

	//@AfterClass
	public static void after() {
		accessToken = openId = null;
	}

	//@Test
	public void testGetUserStringStringStringMap() throws IOException {
		if (openId != null) {
			Map result = new HashMap();
			UserApi.getUser(accessToken, openId,
					Locale.getDefault().toString(), result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("get user result:" + result);
		}
	}

	//@Test
	public void testGetUserStringStringMap() throws IOException {
		if (openId != null) {
			Map result = new HashMap();
			UserApi.getUser(accessToken, openId, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("get user result:" + result);
		}
	}

	//@Test
	public void testGetUserStringStringString() throws IOException {
		if (openId != null) {
			User user = UserApi.getUser(accessToken, openId, Locale
					.getDefault().toString());
			System.out.println("get user:" + user);
		}
	}

	//@Test
	public void testGetUserStringString() throws IOException {
		if (openId != null) {
			User user = UserApi.getUser(accessToken, openId);
			System.out.println("get user:" + user);
		}
	}
	
	//@Test
	public void testGetUsersStringIntInt() throws HttpException, IOException {
		List<String> users=UserApi.getUsers(accessToken, 1, 50);
		Assert.assertNotNull(users);
		System.out.println("get user pageNum,pageSize:"+users);
		users=UserApi.getUsers(accessToken, 101, 100);
		Assert.assertNotNull(users);
		System.out.println("get user pageNum,pageSize:"+users);
	}

	//@Test
	public void testGetUsersStringStringMap() throws HttpException, IOException {
		if (openId != null) {
			Map result = new HashMap();
			UserApi.getUsers(accessToken, openId, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("get users result:" + result);
		}
	}

	//@Test
	public void testGetUsersStringMap() throws HttpException, IOException {
		Map result = new HashMap();
		UserApi.getUsers(accessToken, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("get users result:" + result);
	}

	//@Test
	public void testGetUsersStringString() throws HttpException, IOException {
		if(openId!=null){
			List<String> users=UserApi.getUsers(accessToken, openId);
			Assert.assertNotNull(users);
			System.out.println("get users:" + users);
		}
	}

	//@Test
	public void testGetUsersString() throws HttpException, IOException {
		List<String> users=UserApi.getUsers(accessToken);
		Assert.assertNotNull(users);
		System.out.println("get users:" + users);
	}

}
