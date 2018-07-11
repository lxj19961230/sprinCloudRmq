package com.xier.rabbit.rmq.mq.scene;

import com.xier.rabbit.rmq.mq.AbstractChannel;

/*
 *任务通道类定义：
 *一个发送端，多个接收端，如分布式的任务派发。
 *为了保证消息发送的可靠性，不丢失消息，使消息持久化了。
 *同时为了防止接收端在处理消息时down掉，只有在消息处理完成后才发送ack消息。
 */
public class TaskChannel extends AbstractChannel {
	public TaskChannel() {

	}
	
	@Override
	protected void exchangeDeclare(String exchange) throws Exception { 
//		if(property.getQueue()==null && property.getQueue().isEmpty()){
//			property.setQueue(channel.queueDeclare().getQueue());
//		}
//		channel.queueDeclare(property.getQueue(), property.isDurable(), false,false, null);
	}

	@Override
	public void queueBind(String exchange,String queue,boolean isDurable,String routeKey) throws Exception {
		
	}  
	
	@Override
	public void queueUnBind(String exchange,String queue,String routeKey) throws Exception { 
	}
}