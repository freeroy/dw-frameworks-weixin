package org.developerworld.frameworks.weixin.message.mass;

import org.developerworld.frameworks.weixin.message.MsgType;

public class ImageMassMessage extends AbstractMediaMassMessage {
	
	public MsgType getMsgType() {
		return MsgType.IMAGE;
	}

}
