package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.developerworld.frameworks.weixin.api.dto.Group;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class GroupApiTest {

	private static String accessToken;
	private static String openId;

	@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		List<String> users = UserApi.getUsers(accessToken);
		openId = users.size() > 0 ? users.get(0) : null;
	}

	@AfterClass
	public static void after() {
		accessToken = openId = null;
	}

	//@Test
	public void testCreateGroupStringStringMap() throws HttpException,
			IOException {
		Map result = new HashMap();
		GroupApi.createGroup(accessToken, "测试组", result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("create group result:" + result);
	}

	//@Test
	public void testCreateGroupStringString() throws HttpException, IOException {
		Integer rst = GroupApi.createGroup(accessToken, "测试组");
		System.out.println("create group id:" + rst);
	}

	//@Test
	public void testGetGroupsStringMap() throws HttpException, IOException {
		Map result = new HashMap();
		GroupApi.getGroups(accessToken, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("get groups result:" + result);
	}

	//@Test
	public void testGetGroupsString() throws HttpException, IOException {
		List<Group> groups = GroupApi.getGroups(accessToken);
		Assert.assertNotNull(groups);
		System.out.println("get groups result:" + groups);
	}

	//@Test
	public void testGetUserGroupStringStringMap()
			throws JsonGenerationException, JsonMappingException, IOException {
		if (openId != null) {
			Map result = new HashMap();
			GroupApi.getUserGroup(accessToken, openId, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("get user group result:" + result);
		}
	}

	//@Test
	public void testGetUserGroupStringString() throws JsonGenerationException,
			JsonMappingException, IOException {
		if (openId != null) {
			Integer id = GroupApi.getUserGroup(accessToken, openId);
			System.out.println("get user group id:" + id);
		}
	}

	//@Test
	public void testUpdateGroupNameStringIntegerStringMap()
			throws JsonGenerationException, JsonMappingException,
			HttpException, IOException {
		Map result = new HashMap();
		GroupApi.updateGroupName(accessToken,
				GroupApi.createGroup(accessToken, "测试组"), "测试组-改", result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("update group name result:" + result);
	}

	//@Test
	public void testUpdateGroupNameStringIntegerString()
			throws JsonGenerationException, JsonMappingException,
			HttpException, IOException {
		boolean rst = GroupApi.updateGroupName(accessToken,
				GroupApi.createGroup(accessToken, "测试组"), "测试组-改");
		System.out.println("update group name:" + rst);
	}

	//@Test
	public void testUpdateUserGroupStringStringIntegerMap()
			throws HttpException, IOException {
		if(openId!=null){
			Integer groupId = GroupApi.createGroup(accessToken, "测试组");
			Map result = new HashMap();
			GroupApi.updateUserGroup(accessToken, openId, groupId, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("update user group result:" + result);
		}
	}

	//@Test
	public void testUpdateUserGroupStringStringInteger()
			throws JsonGenerationException, JsonMappingException, IOException {
		if(openId!=null){
			Integer groupId = GroupApi.createGroup(accessToken, "测试组");
			Map result = new HashMap();
			boolean rst = GroupApi.updateUserGroup(accessToken, openId, groupId);
			System.out.println("update user group:" + rst);
		}
	}

}
