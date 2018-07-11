package com.xier.rabbit.rmq.mq.scene;

import com.xier.rabbit.rmq.mq.AbstractChannel;

/*
 *发布/订阅 通道类定义：
 *发送端发送广播消息，多个接收端接收。
 */
public class BroadcastChannel extends AbstractChannel {
	public BroadcastChannel() {

	}
	
	@Override
	protected void exchangeDeclare(String exchange) throws Exception { 
		channel.exchangeDeclare(exchange, "fanout"); 
	}

	@Override
	public void queueBind(String exchange,String queue,boolean isDurable,String routeKey) throws Exception {
		channel.queueDeclare(queue,isDurable,false,false, null);
		channel.queueBind(queue, exchange, routeKey);
		queueMap.put(queue, false);		
	}
	
	@Override
	public void queueUnBind(String exchange,String queue,String routeKey) throws Exception {
		channel.queueUnbind(queue, exchange, routeKey);
		queueMap.remove(queue);
	}
}
