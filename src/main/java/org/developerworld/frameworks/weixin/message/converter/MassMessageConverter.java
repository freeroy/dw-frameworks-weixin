package org.developerworld.frameworks.weixin.message.converter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.developerworld.commons.lang.MapBuilder;
import org.developerworld.frameworks.weixin.message.MassMessage;
import org.developerworld.frameworks.weixin.message.mass.AbstractMediaMassMessage;
import org.developerworld.frameworks.weixin.message.mass.CsNewsMassMessage;
import org.developerworld.frameworks.weixin.message.mass.TextMassMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 客服信息转换器
 * 
 * @author Roy Huang
 * @version 20140310
 * 
 */
public class MassMessageConverter {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 转换json字符
	 * 
	 * @param massMessage
	 * @return
	 * @throws IOException
	 */
	public String convertToAllJson(MassMessage massMessage) throws IOException {
		Map root = new LinkedHashMap();
		Map filter = new LinkedHashMap();
		filter.put("is_to_all", true);
		root.put("filter", filter);
		root.putAll(buildMessage(massMessage));
		return objectMapper.writeValueAsString(root);
	}

	/**
	 * 转换json字符
	 * 
	 * @param massMessage
	 * @param groupId
	 * @return
	 * @throws IOException
	 */
	public String convertToGroupJson(MassMessage massMessage, String groupId) throws IOException {
		Map root = new LinkedHashMap();
		Map filter = new LinkedHashMap();
		filter.put("group_id", groupId);
		root.put("filter", filter);
		root.putAll(buildMessage(massMessage));
		return objectMapper.writeValueAsString(root);
	}

	/**
	 * 转换json字符串
	 * 
	 * @param massMessage
	 * @param tousers
	 * @return
	 * @throws IOException
	 */
	public String convertToToUsersJson(MassMessage massMessage, List<String> tousers) throws IOException {
		Map root = new LinkedHashMap();
		root.put("touser", tousers);
		root.putAll(buildMessage(massMessage));
		return objectMapper.writeValueAsString(root);
	}

	/**
	 * 转换json字符串
	 * 
	 * @param massMessage
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	public String convertToToUserJson(MassMessage massMessage, String openId) throws IOException {
		Map root = new LinkedHashMap();
		root.put("touser", openId);
		root.putAll(buildMessage(massMessage));
		return objectMapper.writeValueAsString(root);
	}

	/**
	 * 创建信息对象
	 * 
	 * @param massMessage
	 * @return
	 */
	private Map buildMessage(MassMessage massMessage) {
		Map rst = new LinkedHashMap();
		if (massMessage != null) {
			if (massMessage instanceof AbstractMediaMassMessage) {
				AbstractMediaMassMessage _msg = (AbstractMediaMassMessage) massMessage;
				rst.put(_msg.getMsgType().toString(), new MapBuilder().put("media_id", _msg.getMediaId()).map());
			} else if (massMessage instanceof TextMassMessage) {
				TextMassMessage _msg = (TextMassMessage) massMessage;
				rst.put("text", new MapBuilder().put("content", _msg.getContent()).map());
			} else if (massMessage instanceof CsNewsMassMessage) {
				CsNewsMassMessage _msg = (CsNewsMassMessage) massMessage;
				rst.put("news", new MapBuilder().put("articles", _msg.getNewsCsMessage().getAtricles()).map());
			}
			rst.put("msgtype", massMessage.getMsgType().toString());
		}
		return rst;
	}

}
