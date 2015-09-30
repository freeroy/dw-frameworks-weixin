package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.developerworld.frameworks.weixin.api.dto.MediaType;
import org.developerworld.frameworks.weixin.api.dto.NewsArticle;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MassApiTest {

	private static String accessToken;
	private static String openId;
	private static String mediaId;
	private static Integer groupId;

	@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		List<String> users = UserApi.getUsers(accessToken);
		openId = users.size() > 0 ? users.get(0) : null;

		File thumbFile = new File(MediaApiTest.class.getResource(
				"/media/thumb.jpg").getFile());
		String thumbMediaId = MediaApi.uploadMedia(accessToken,
				MediaType.THUMB, thumbFile);

		List<NewsArticle> articles = new ArrayList<NewsArticle>();
		NewsArticle newsArticle = new NewsArticle();
		newsArticle.setAuthor("author");
		newsArticle.setContent("content");
		newsArticle.setContentSourceUrl("contentSourceUrl");
		newsArticle.setDigest("digest");
		newsArticle.setThumbMediaId(thumbMediaId);
		newsArticle.setTitle("title");
		articles.add(newsArticle);
		mediaId = MediaApi.uploadNews(accessToken, articles);
		groupId = GroupApi.getGroups(accessToken).get(0).getId();
	}

	@AfterClass
	public static void after() {
		accessToken = openId = mediaId = null;
		groupId = null;
	}

	//@Test
	public void testSendUsersMessageStringListOfStringString()
			throws HttpException, IOException {
		List<String> openIds = new ArrayList<String>();
		openIds.add(openId);
		Long msgId = MassApi.sendUsersMessage(accessToken, openIds, mediaId);
		//Assert.assertNotNull(msgId);
	}

	//@Test
	public void testSendUsersMessageStringListOfStringStringMap()
			throws HttpException, IOException {
		Map result = new HashMap();
		List<String> openIds = new ArrayList<String>();
		openIds.add(openId);
		MassApi.sendUsersMessage(accessToken, openIds, mediaId, result);
		Assert.assertTrue(result.size() > 0);
	}

	//@Test
	public void testSendGroupMessageStringStringString() throws HttpException,
			IOException {
		Long msgId = MassApi.sendGroupMessage(accessToken, groupId.toString(),
				mediaId);
		//Assert.assertNotNull(msgId);
	}

	//@Test
	public void testSendGroupMessageStringStringStringMap()
			throws HttpException, IOException {
		Map result = new HashMap();
		if (openId != null) {
			List<String> openIds = new ArrayList<String>();
			openIds.add(openId);
			MassApi.sendGroupMessage(accessToken, groupId.toString(), mediaId,
					result);
			Assert.assertTrue(result.size() > 0);
		}
	}

	//@Test
	public void testDeleteMassStringLong() throws JsonGenerationException,
			JsonMappingException, IOException {
		Long msgId = MassApi.sendGroupMessage(accessToken, groupId.toString(),
				mediaId);
		if (msgId != null) {
			boolean rst = MassApi.deleteMass(accessToken, msgId);
//			Assert.assertTrue(rst);
		}
	}

	//@Test
	public void testDeleteMassStringLongMap() throws HttpException, IOException {
		Long msgId = MassApi.sendGroupMessage(accessToken, groupId.toString(),
				mediaId);
		if (msgId != null) {
			Map result = new HashMap();
			MassApi.deleteMass(accessToken, msgId, result);
			Assert.assertTrue(result.size() > 0);
		}
	}

}
