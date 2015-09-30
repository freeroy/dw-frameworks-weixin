package org.developerworld.frameworks.weixin.message.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.message.CsMessage;
import org.developerworld.frameworks.weixin.message.MsgType;
import org.developerworld.frameworks.weixin.message.cs.AbstractCsMessage;
import org.developerworld.frameworks.weixin.message.cs.ImageCsMessage;
import org.developerworld.frameworks.weixin.message.cs.MusicCsMessage;
import org.developerworld.frameworks.weixin.message.cs.NewsCsMessage;
import org.developerworld.frameworks.weixin.message.cs.NewsCsMessage.Article;
import org.developerworld.frameworks.weixin.message.cs.TextCsMessage;
import org.developerworld.frameworks.weixin.message.cs.VideoCsMessage;
import org.developerworld.frameworks.weixin.message.cs.VoiceCsMessage;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 客服信息转换器
 * 
 * @author Roy Huang
 * @version 20140310
 * 
 */
public class CsMessageConverter {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 转换为json
	 * @param csMessage
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String convertToJson(CsMessage csMessage)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map msg = new LinkedHashMap();
		if (csMessage instanceof AbstractCsMessage) {
			AbstractCsMessage _msg = (AbstractCsMessage) csMessage;
			msg.put("touser", _msg.getToUser());
		}
		if (csMessage instanceof TextCsMessage) {
			msg.put("msgtype",MsgType.TEXT.toString());
			TextCsMessage _msg = (TextCsMessage) csMessage;
			Map text = new LinkedHashMap();
			text.put("content", _msg.getContent());
			msg.put("text", text);
		}
		else if (csMessage instanceof ImageCsMessage) {
			msg.put("msgtype",MsgType.IMAGE.toString());
			ImageCsMessage _msg = (ImageCsMessage) csMessage;
			Map image = new LinkedHashMap();
			image.put("media_id", _msg.getMediaId());
			msg.put("image", image);
		}
		else if (csMessage instanceof VoiceCsMessage) {
			msg.put("msgtype",MsgType.VOICE.toString());
			VoiceCsMessage _msg = (VoiceCsMessage) csMessage;
			Map voice = new LinkedHashMap();
			voice.put("media_id", _msg.getMediaId());
			msg.put("voice", voice);
		}
		else if (csMessage instanceof VideoCsMessage) {
			msg.put("msgtype",MsgType.VIDEO.toString());
			VideoCsMessage _msg = (VideoCsMessage) csMessage;
			Map video = new LinkedHashMap();
			video.put("media_id", _msg.getMediaId());
			if (StringUtils.isNotBlank(_msg.getTitle()))
				video.put("title", _msg.getTitle());
			if (StringUtils.isNotBlank(_msg.getDescription()))
				video.put("description", _msg.getDescription());
			msg.put("video", video);
		}
		else if (csMessage instanceof MusicCsMessage) {
			msg.put("msgtype",MsgType.MUSIC.toString());
			MusicCsMessage _msg = (MusicCsMessage) csMessage;
			Map music = new LinkedHashMap();
			music.put("title", _msg.getTitle());
			music.put("description", _msg.getDescription());
			music.put("musicurl", _msg.getMusicUrl());
			music.put("hqmusicurl", _msg.getHqMusicUrl());
			music.put("thumb_media_id", _msg.getThumbMediaId());
			msg.put("music", music);
		}
		else if (csMessage instanceof NewsCsMessage) {
			msg.put("msgtype",MsgType.NEWS.toString());
			NewsCsMessage _msg = (NewsCsMessage) csMessage;
			Map news = new LinkedHashMap();
			List<Map> articles = new ArrayList<Map>();
			for (Article article : _msg.getAtricles()) {
				Map _article = new LinkedHashMap();
				if (StringUtils.isNotBlank(article.getTitle()))
					_article.put("title", article.getTitle());
				if (StringUtils.isNotBlank(article.getDescription()))
					_article.put("description", article.getDescription());
				if (StringUtils.isNotBlank(article.getUrl()))
					_article.put("url", article.getUrl());
				if (StringUtils.isNotBlank(article.getPicUrl()))
					_article.put("picurl", article.getPicUrl());
				articles.add(_article);
			}
			news.put("articles", articles);
			msg.put("news", news);
		}
		return objectMapper.writeValueAsString(msg);
	}
}
