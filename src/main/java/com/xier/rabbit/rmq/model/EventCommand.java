package com.xier.rabbit.rmq.model;

import com.xier.rabbit.rmq.handler.Event;

public abstract class EventCommand implements Event {
     private String msgId;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}


	
}
