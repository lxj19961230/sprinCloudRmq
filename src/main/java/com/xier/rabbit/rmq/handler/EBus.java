package com.xier.rabbit.rmq.handler;

import com.xier.rabbit.rmq.mq.ChannelBootstrap;

/**
 * 事件总线
 * 
 */
/**
 * @author Administrator
 *
 */
public final class EBus {

	private static EBus INSTANCE = new EBus();

	private ChannelBootstrap channel;

	private EBus() {
			channel = new ChannelBootstrap().start();
	}

	
	/**
	 * 订阅事件
	 * @param subscriber
	 * @return
	 */
	public static int eventListener(Object subscriber) {
		if (subscriber == null) {
			return EventErrorCode.SUBSCRIBE_LISTEN_INVALID;
		}
		if(INSTANCE.channel==null){
			return EventErrorCode.CHANNEL_INITIALIZE_EXCEPTION;
		}
		return INSTANCE.channel.register(subscriber);
	}

	/**
	 * 发布同步事件
	 * 
	 * @param event
	 * @param isSync
	 */
	public static int publishSyn(Object event) {
		return doPublish(event, true);
	}
 
	/**
	 * 发布异步事件
	 * 
	 * @param event
	 */
	public static int publish(Object event) {
		return doPublish(event, false);
	}

	private static int doPublish(Object event, boolean isSync) {
		if (event == null) {
			return EventErrorCode.INVALID_MESSAGE;
		}
		if(INSTANCE.channel==null){
			return EventErrorCode.CHANNEL_INITIALIZE_EXCEPTION;
		}
		return INSTANCE.channel.publish(event, isSync);
	}
}
