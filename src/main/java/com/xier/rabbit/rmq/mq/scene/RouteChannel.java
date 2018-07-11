package com.xier.rabbit.rmq.mq.scene;

import com.xier.rabbit.rmq.mq.AbstractChannel;

/*
 *路由模式通道类定义：
 *发送端按routing key发送消息，不同的接收端按不同的routing key接收消息；
 *比如消息日志debug在控制台打印，error保存到文件中，这时要启动两个客户端段，
 *一个接收debug日志，在控制台打印，一个接收error日志，写入文件。
 */
public class RouteChannel extends AbstractChannel {
	public RouteChannel() {

	}
	
	@Override
	protected void exchangeDeclare(String exchange) throws Exception { 
		channel.exchangeDeclare(exchange, "direct");
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