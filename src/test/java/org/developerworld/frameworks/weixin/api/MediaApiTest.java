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
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MediaApiTest {

	private static String accessToken;
	private static File imageFile;
	private static File thumbFile;
	private static File videoFile;
	private static File voiceFile;
	private static File voiceFile2;
	private static String thumbMediaId;

//	@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		imageFile = new File(MediaApiTest.class.getResource("/media/image.jpg")
				.getFile());
		thumbFile = new File(MediaApiTest.class.getResource("/media/thumb.jpg")
				.getFile());
		voiceFile = new File(MediaApiTest.class.getResource("/media/voice.amr")
				.getFile());
		voiceFile2 = new File(MediaApiTest.class.getResource("/media/voice.mp3")
				.getFile());
		videoFile = new File(MediaApiTest.class.getResource("/media/video.mp4")
				.getFile());
		thumbMediaId=MediaApi.uploadMedia(accessToken, MediaType.THUMB, thumbFile);
	}

//	@AfterClass
	public static void after() {
		accessToken = null;
		imageFile = thumbFile = videoFile=voiceFile2 = voiceFile =null;
		thumbMediaId= null;
	}

	//@Test
	public void testUploadMediaStringMediaTypeFile() throws HttpException,
			IOException {
		MediaType[] types = { MediaType.IMAGE, MediaType.THUMB, MediaType.VOICE, MediaType.VOICE,
				MediaType.VIDEO };
		File[] files = { imageFile, thumbFile,voiceFile,voiceFile2, videoFile };
		for (int i = 0; i < types.length; i++) {
			MediaType type = types[i];
			File file = files[i];
			String mediaId = MediaApi.uploadMedia(accessToken, type, file);
			System.out.println("upload media id:" + mediaId);
		}
	}

	//@Test
	public void testUploadMediaStringMediaTypeFileMap() throws HttpException,
			IOException {
		MediaType[] types = { MediaType.IMAGE, MediaType.THUMB, MediaType.VOICE, MediaType.VOICE,
				MediaType.VIDEO };
		File[] files = { imageFile, thumbFile,voiceFile,voiceFile2, videoFile };
		for (int i = 0; i < types.length; i++) {
			MediaType type = types[i];
			File file = files[i];
			Map result = new HashMap();
			MediaApi.uploadMedia(accessToken, type, file, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("upload media result:" + result);
		}
	}

	//@Test
	public void testGetMediaUrl() throws HttpException, IOException {
		String mediaId = MediaApi.uploadMedia(accessToken, MediaType.IMAGE,
				imageFile);
		if (mediaId != null) {
			String url = MediaApi.getMediaUrl(accessToken, mediaId);
			System.out.println("get media url:" + url);
		}
	}

	//@Test
	public void testGetMediaStringString() throws HttpException, IOException {
		String mediaId = MediaApi.uploadMedia(accessToken, MediaType.IMAGE,
				imageFile);
		if (mediaId != null) {
			byte[] datas = MediaApi.getMedia(accessToken, mediaId);
			System.out.println("get media:" + datas);
		}
	}

	//@Test
	public void testGetMediaStringStringMap() throws HttpException, IOException {
		String mediaId = MediaApi.uploadMedia(accessToken, MediaType.IMAGE,
				imageFile);
		if (mediaId != null) {
			Map result = new HashMap();
			MediaApi.getMedia(accessToken, mediaId, result);
			Assert.assertTrue(result.size() > 0);
			System.out.println("get media result:" + result);
		}
	}
	
//	@Test
	public void testUploadNewsStringList() throws JsonGenerationException, JsonMappingException, IOException{
		List<NewsArticle> articles=new ArrayList<NewsArticle>();
		NewsArticle newsArticle=new NewsArticle();
		newsArticle.setAuthor("author");
		newsArticle.setContent("content");
		newsArticle.setContentSourceUrl("contentSourceUrl");
		newsArticle.setDigest("digest");
		newsArticle.setThumbMediaId(thumbMediaId);
		newsArticle.setTitle("title");
		articles.add(newsArticle);
		String msgId=MediaApi.uploadNews(accessToken, articles);
		Assert.assertNotNull(msgId);
	}
	
//	@Test
	public void testUploadNewsStringListMap() throws JsonGenerationException, JsonMappingException, IOException{
		List<NewsArticle> articles=new ArrayList<NewsArticle>();
		NewsArticle newsArticle=new NewsArticle();
		newsArticle.setAuthor("author");
		newsArticle.setContent("content");
		newsArticle.setContentSourceUrl("contentSourceUrl");
		newsArticle.setDigest("digest");
		newsArticle.setThumbMediaId(thumbMediaId);
		newsArticle.setTitle("title");
		articles.add(newsArticle);
		
		Map result=new HashMap();
		MediaApi.uploadNews(accessToken, articles, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("upload news result:" + result);
	}

}
