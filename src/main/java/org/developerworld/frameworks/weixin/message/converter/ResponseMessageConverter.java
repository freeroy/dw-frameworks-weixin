package org.developerworld.frameworks.weixin.message.converter;

import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.message.MsgType;
import org.developerworld.frameworks.weixin.message.ResponseMessage;
import org.developerworld.frameworks.weixin.message.response.AbstractResponseMessage;
import org.developerworld.frameworks.weixin.message.response.CustomerServiceResponseMessage;
import org.developerworld.frameworks.weixin.message.response.ImageResponseMessage;
import org.developerworld.frameworks.weixin.message.response.MusicResponseMessage;
import org.developerworld.frameworks.weixin.message.response.NewsResponseMessage;
import org.developerworld.frameworks.weixin.message.response.NewsResponseMessage.Article;
import org.developerworld.frameworks.weixin.message.response.TextResponseMessage;
import org.developerworld.frameworks.weixin.message.response.VideoResponseMessage;
import org.developerworld.frameworks.weixin.message.response.VoiceResponseMessage;

/**
 * 响应信息转换器
 * 
 * @author Roy Huang
 * @version 20140310
 * 
 */
public class ResponseMessageConverter {

	/**
	 * 对象转换为xml
	 * 
	 * @param responseMessage
	 * @return
	 */
	public String convertToXml(ResponseMessage responseMessage) {
		StringBuilder rst = null;
		if (responseMessage != null) {
			rst = new StringBuilder().append("<xml>");
			if (responseMessage instanceof AbstractResponseMessage) {
				AbstractResponseMessage _msg = (AbstractResponseMessage) responseMessage;
				rst.append("<ToUserName><![CDATA[")
						.append(fixXml(_msg.getToUserName()))
						.append("]]></ToUserName>");
				rst.append("<FromUserName><![CDATA[")
						.append(fixXml(_msg.getFromUserName()))
						.append("]]></FromUserName>");
				rst.append("<CreateTime>").append(_msg.getCreateTime())
						.append("</CreateTime>");
			}
			if (responseMessage instanceof TextResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.TEXT.toString())
						.append("]]></MsgType>");
				TextResponseMessage _msg = (TextResponseMessage) responseMessage;
				rst.append("<Content><![CDATA[")
						.append(fixXml(_msg.getContent()))
						.append("]]></Content>");

			} else if (responseMessage instanceof ImageResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.IMAGE.toString())
						.append("]]></MsgType>");
				ImageResponseMessage _msg = (ImageResponseMessage) responseMessage;
				rst.append("<Image><MediaId><![CDATA[")
						.append(fixXml(_msg.getMediaId()))
						.append("]]></MediaId></Image>");
			} else if (responseMessage instanceof VoiceResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.VOICE.toString())
						.append("]]></MsgType>");
				VoiceResponseMessage _msg = (VoiceResponseMessage) responseMessage;
				rst.append("<Voice><MediaId><![CDATA[")
						.append(fixXml(_msg.getMediaId()))
						.append("]]></MediaId></Voice>");
			} else if (responseMessage instanceof VideoResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.VIDEO.toString())
						.append("]]></MsgType>");
				VideoResponseMessage _msg = (VideoResponseMessage) responseMessage;
				rst.append("<Video>").append("<MediaId><![CDATA[")
						.append(fixXml(_msg.getMediaId()))
						.append("]]></MediaId>");
				if (StringUtils.isNotBlank(_msg.getTitle()))
					rst.append("<Title><![CDATA[")
							.append(fixXml(_msg.getTitle()))
							.append("]]></Title>");
				if (StringUtils.isNotBlank(_msg.getDescription()))
					rst.append("<Description><![CDATA[")
							.append(fixXml(_msg.getDescription()))
							.append("]]></Description>");
				rst.append("</Video>");
			} else if (responseMessage instanceof MusicResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.MUSIC.toString())
						.append("]]></MsgType>");
				MusicResponseMessage _msg = (MusicResponseMessage) responseMessage;
				rst.append("<Music>");
				if (StringUtils.isNotBlank(_msg.getTitle()))
					rst.append("<Title><![CDATA[")
							.append(fixXml(_msg.getTitle()))
							.append("]]></Title>");
				if (StringUtils.isNotBlank(_msg.getDescription()))
					rst.append("<Description><![CDATA[")
							.append(fixXml(_msg.getDescription()))
							.append("]]></Description>");
				if (StringUtils.isNotBlank(_msg.getMusicUrl()))
					rst.append("<MusicUrl><![CDATA[")
							.append(fixXml(_msg.getMusicUrl()))
							.append("]]></MusicUrl>");
				if (StringUtils.isNotBlank(_msg.getHqMusicUrl()))
					rst.append("<HQMusicUrl><![CDATA[")
							.append(fixXml(_msg.getHqMusicUrl()))
							.append("]]></HQMusicUrl>");
				rst.append("<ThumbMediaId><![CDATA[")
						.append(fixXml(_msg.getThumbMediaId()))
						.append("]]></ThumbMediaId>").append("</Music>");
			} else if (responseMessage instanceof NewsResponseMessage) {
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.NEWS.toString())
						.append("]]></MsgType>");
				NewsResponseMessage _msg = (NewsResponseMessage) responseMessage;
				rst.append("<ArticleCount>").append(_msg.getArticleCount())
						.append("</ArticleCount>").append("<Articles>");
				for (Article article : _msg.getArticles()) {
					rst.append("<item>");
					if (StringUtils.isNotBlank(article.getTitle()))
						rst.append("<Title><![CDATA[")
								.append(fixXml(article.getTitle()))
								.append("]]></Title>");
					if (StringUtils.isNotBlank(article.getDescription()))
						rst.append("<Description><![CDATA[")
								.append(fixXml(article.getDescription()))
								.append("]]></Description>");
					if (StringUtils.isNotBlank(article.getPicUrl()))
						rst.append("<PicUrl><![CDATA[")
								.append(fixXml(article.getPicUrl()))
								.append("]]></PicUrl>");
					if (StringUtils.isNotBlank(article.getUrl()))
						rst.append("<Url><![CDATA[")
								.append(fixXml(article.getUrl()))
								.append("]]></Url>");
					rst.append("</item>");
				}

				rst.append("</Articles>");
			} else if (responseMessage instanceof CustomerServiceResponseMessage) {
				CustomerServiceResponseMessage _msg = (CustomerServiceResponseMessage) responseMessage;
				rst.append("<MsgType><![CDATA[")
						.append(MsgType.TRANSFERCUSTOMERSERVICE.toString())
						.append("]]></MsgType>");
				if (StringUtils.isNotBlank(_msg.getKfAccount())) {
					rst.append("<TransInfo>");
					rst.append("<KfAccount><![CDATA[")
							.append(fixXml(_msg.getKfAccount()))
							.append("]]></KfAccount>");
					rst.append("</TransInfo>");
				}
			}
			rst.append("</xml>");
		}
		return rst.toString();
	}

	/**
	 * 修正xml内容
	 * 
	 * @param value
	 * @return
	 */
	private String fixXml(String value) {
		return value;
		// if (value == null)
		// return value;
		// return value.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
		// .replaceAll("&", "&amp;").replaceAll("'", "&apos;")
		// .replaceAll("\"", "&quot;");
	}

}
