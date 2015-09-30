package org.developerworld.frameworks.weixin.message.converter;

import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.weixin.message.EventType;
import org.developerworld.frameworks.weixin.message.MsgType;
import org.developerworld.frameworks.weixin.message.RequestMessage;
import org.developerworld.frameworks.weixin.message.request.AbstractRequestMessage;
import org.developerworld.frameworks.weixin.message.request.EventRequestMessage;
import org.developerworld.frameworks.weixin.message.request.ImageRequestMessage;
import org.developerworld.frameworks.weixin.message.request.LinkRequestMessage;
import org.developerworld.frameworks.weixin.message.request.LocationEventRequestMessage;
import org.developerworld.frameworks.weixin.message.request.LocationRequestMessage;
import org.developerworld.frameworks.weixin.message.request.MassEventRequestMessage;
import org.developerworld.frameworks.weixin.message.request.MenuEventRequestMessage;
import org.developerworld.frameworks.weixin.message.request.QrCodeEventRequestMessage;
import org.developerworld.frameworks.weixin.message.request.TextRequestMessage;
import org.developerworld.frameworks.weixin.message.request.VideoRequestMessage;
import org.developerworld.frameworks.weixin.message.request.VoiceRequestMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 信息构建器
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class RequestMessageConverter {

	/**
	 * xml转换为对象
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public RequestMessage convertToObject(String xml) throws DocumentException {
		RequestMessage rst = null;
		if (StringUtils.isNotBlank(xml)) {
			Document doc = DocumentHelper.parseText(xml);
			Element e = doc.getRootElement();
			String msgType = e.elementText("MsgType");
			if (StringUtils.isNotBlank(msgType)) {
				if (msgType.equalsIgnoreCase(MsgType.TEXT.toString())) {
					TextRequestMessage _msg = new TextRequestMessage();
					_msg.setContent(e.elementText("Content"));
					rst = _msg;
				} else if (msgType.equalsIgnoreCase(MsgType.IMAGE.toString())) {
					ImageRequestMessage _msg = new ImageRequestMessage();
					_msg.setMediaId(e.elementText("MediaId"));
					_msg.setPicUrl(e.elementText("PicUrl"));
					rst = _msg;
				} else if (msgType.equalsIgnoreCase(MsgType.VOICE.toString())) {
					VoiceRequestMessage _msg = new VoiceRequestMessage();
					_msg.setMediaId(e.elementText("MediaId"));
					_msg.setFormat(e.elementText("Format"));
					rst = _msg;
				} else if (msgType.equalsIgnoreCase(MsgType.VIDEO.toString())) {
					VideoRequestMessage _msg = new VideoRequestMessage();
					_msg.setMediaId(e.elementText("MediaId"));
					_msg.setThumbMediaId(e.elementText("ThumbMediaId"));
					_msg.setThumbMediaId(e.elementText("ThumbMediaId"));
					rst = _msg;
				} else if (msgType
						.equalsIgnoreCase(MsgType.LOCATION.toString())) {
					LocationRequestMessage _msg = new LocationRequestMessage();
					_msg.setLocationX(Double.valueOf(e
							.elementText("Location_X")));
					_msg.setLocationY(Double.valueOf(e
							.elementText("Location_Y")));
					_msg.setScale(Integer.valueOf(e.elementText("Scale")));
					_msg.setLabel(e.elementText("Label"));
					rst = _msg;
				} else if (msgType.equalsIgnoreCase(MsgType.LINK.toString())) {
					LinkRequestMessage _msg = new LinkRequestMessage();
					_msg.setTitle(e.elementText("Title"));
					_msg.setDescription(e.elementText("Description"));
					_msg.setUrl(e.elementText("Url"));
					rst = _msg;
				} else if (msgType.equalsIgnoreCase(MsgType.EVENT.toString())) {
					String event = e.elementText("Event");
					if (StringUtils.isNotBlank(event)) {
						if (event.equalsIgnoreCase(EventType.SUBSCRIBE
								.toString())) {
							// 有两种关注方式，一般客户端关注和二位码关注
							if (StringUtils.isNotBlank(e
									.elementText("EventKey"))) {
								// 二维码方式
								QrCodeEventRequestMessage _msg = new QrCodeEventRequestMessage();
								_msg.setEvent(EventType.SUBSCRIBE);
								_msg.setEventKey(e.elementText("EventKey"));
								_msg.setTicket(e.elementText("Ticket"));
								rst = _msg;
							} else {
								EventRequestMessage _msg = new EventRequestMessage();
								_msg.setEvent(EventType.SUBSCRIBE);
								rst = _msg;
							}
						} else if (event.equalsIgnoreCase(EventType.UNSUBSCRIBE
								.toString())) {
							EventRequestMessage _msg = new EventRequestMessage();
							_msg.setEvent(EventType.UNSUBSCRIBE);
							rst = _msg;
						} else if (event.equalsIgnoreCase(EventType.CLICK
								.toString())) {
							MenuEventRequestMessage _msg = new MenuEventRequestMessage();
							_msg.setEvent(EventType.CLICK);
							_msg.setEventKey(e.elementText("EventKey"));
							rst = _msg;
						} else if (event.equalsIgnoreCase(EventType.VIEW
								.toString())) {
							MenuEventRequestMessage _msg = new MenuEventRequestMessage();
							_msg.setEvent(EventType.VIEW);
							_msg.setEventKey(e.elementText("EventKey"));
							rst = _msg;
						} else if (event.equalsIgnoreCase(EventType.LOCATION
								.toString())) {
							LocationEventRequestMessage _msg = new LocationEventRequestMessage();
							_msg.setEvent(EventType.LOCATION);
							_msg.setLatitude(Double.valueOf(e
									.elementText("Latitude")));
							_msg.setLongitude(Double.valueOf(e
									.elementText("Longitude")));
							_msg.setPrecision(Double.valueOf(e
									.elementText("Precision")));
							rst = _msg;
						} else if (event.equalsIgnoreCase(EventType.SCAN
								.toString())) {
							QrCodeEventRequestMessage _msg = new QrCodeEventRequestMessage();
							_msg.setEvent(EventType.SCAN);
							_msg.setEventKey(e.elementText("EventKey"));
							_msg.setTicket(e.elementText("Ticket"));
							rst = _msg;
						} else if (event
								.equalsIgnoreCase(EventType.MASSSENDJOBFINISH
										.toString())) {
							MassEventRequestMessage _msg = new MassEventRequestMessage();
							_msg.setEvent(EventType.MASSSENDJOBFINISH);
							_msg.setErrorCount(Long.valueOf(e
									.elementText("ErrorCount")));
							_msg.setFilterCount(Long.valueOf(e
									.elementText("FilterCount")));
							_msg.setMsgId(Long.valueOf(e.elementText("MsgID")));
							_msg.setSentCount(Long.valueOf(e
									.elementText("SentCount")));
							_msg.setStatus(e.elementText("status"));
							_msg.setTotalCount(Long.valueOf(e
									.elementText("TotalCount")));
							rst = _msg;
						}
						if (rst != null && rst instanceof EventRequestMessage) {
							EventRequestMessage _msg = (EventRequestMessage) rst;
							_msg.setFromUserName(e.elementText("FromUserName"));
							_msg.setToUserName(e.elementText("ToUserName"));
							_msg.setCreateTime(Long.parseLong(e
									.elementText("CreateTime")));
							rst = _msg;
						}
					}
				}
				if (rst != null && rst instanceof AbstractRequestMessage) {
					AbstractRequestMessage _msg = (AbstractRequestMessage) rst;
					_msg.setMsgId(Long.parseLong(e.elementText("MsgId")));
					_msg.setFromUserName(e.elementText("FromUserName"));
					_msg.setToUserName(e.elementText("ToUserName"));
					_msg.setCreateTime(Long.parseLong(e
							.elementText("CreateTime")));
				}
			}
		}
		return rst;
	}
}
