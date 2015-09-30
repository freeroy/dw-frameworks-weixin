package org.developerworld.frameworks.weixin.message.mass;

import org.developerworld.frameworks.weixin.message.MsgType;

public class MpNewsMassMessage extends AbstractMediaMassMessage{

	public MsgType getMsgType() {
		return MsgType.MPNEWS;
	}

}
