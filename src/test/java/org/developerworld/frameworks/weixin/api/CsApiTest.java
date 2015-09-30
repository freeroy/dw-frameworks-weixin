package org.developerworld.frameworks.weixin.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.developerworld.frameworks.weixin.api.dto.MediaType;
import org.developerworld.frameworks.weixin.message.cs.ImageCsMessage;
import org.developerworld.frameworks.weixin.message.cs.MusicCsMessage;
import org.developerworld.frameworks.weixin.message.cs.NewsCsMessage;
import org.developerworld.frameworks.weixin.message.cs.NewsCsMessage.Article;
import org.developerworld.frameworks.weixin.message.cs.TextCsMessage;
import org.developerworld.frameworks.weixin.message.cs.VideoCsMessage;
import org.developerworld.frameworks.weixin.message.cs.VoiceCsMessage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CsApiTest {

	private static String accessToken;
	private static String openId;
	private static File imageFile;
	private static File videoFile;
	private static File voiceFile;

	@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		List<String> users = UserApi.getUsers(accessToken);
		openId = users.size() > 0 ? users.get(0) : null;
		imageFile = new File(MediaApiTest.class.getResource("/media/image.jpg")
				.getFile());
		voiceFile = new File(MediaApiTest.class.getResource("/media/voice.amr")
				.getFile());
		videoFile = new File(MediaApiTest.class.getResource("/media/video.mp4")
				.getFile());
	}

	@AfterClass
	public static void after() {
		accessToken = openId = null;
		imageFile = videoFile = voiceFile = null;
	}

	//@Test
	public void testSendMessage() throws HttpException, IOException {
		TextCsMessage message1 = new TextCsMessage();
		message1.setToUser(openId);
		message1.setContent("text");
		CsApi.sendMessage(accessToken, message1);

		ImageCsMessage message2 = new ImageCsMessage();
		String imageMediaId = MediaApi.uploadMedia(accessToken,
				MediaType.IMAGE, imageFile);
		if (imageMediaId != null) {
			message2.setToUser(openId);
			message2.setMediaId(imageMediaId);
			CsApi.sendMessage(accessToken, message2);
		}

		MusicCsMessage message3 = new MusicCsMessage();
		String thumbMediaId = MediaApi.uploadMedia(accessToken,
				MediaType.IMAGE, imageFile);
		if (thumbMediaId != null) {
			message3.setToUser(openId);
			message3.setTitle("title");
			message3.setDescription("description");
			message3.setHqMusicUrl("http//www.developerworld.org/HqMusic.mp3");
			message3.setMusicUrl("http//www.developerworld.org/Music.mp3");
			message3.setThumbMediaId(thumbMediaId);
			CsApi.sendMessage(accessToken, message3);
		}

		VoiceCsMessage message4 = new VoiceCsMessage();
		String voiceMediaId = MediaApi.uploadMedia(accessToken,
				MediaType.VOICE, voiceFile);
		if (voiceMediaId != null) {
			message4.setToUser(openId);
			message4.setMediaId(voiceMediaId);
			CsApi.sendMessage(accessToken, message4);
		}

		VideoCsMessage message5 = new VideoCsMessage();
		String videoMediaId = MediaApi.uploadMedia(accessToken,
				MediaType.VIDEO, videoFile);
		if (videoMediaId != null) {
			message5.setToUser(openId);
			message5.setDescription("description");
			message5.setTitle("title");
			message5.setMediaId(videoMediaId);
			CsApi.sendMessage(accessToken, message5);
		}

		NewsCsMessage message6 = new NewsCsMessage();
		message6.setToUser(openId);
		Article article = new Article();
		article.setDescription("description");
		article.setPicUrl("http://www.developerworld.org/aaa.jpg");
		article.setTitle("title");
		article.setUrl("http://www.163.com");
		message6.getAtricles().add(article);
		article = new Article();
		article.setDescription("description");
		article.setPicUrl("http://www.developerworld.org/aaa.jpg");
		article.setTitle("title");
		article.setUrl("http://www.163.com");
		message6.getAtricles().add(article);
		CsApi.sendMessage(accessToken, message6);
	}

}
