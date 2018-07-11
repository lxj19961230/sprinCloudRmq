package com.xier.rabbit.rmq.mq.scene;

import com.xier.rabbit.rmq.mq.AbstractChannel;

/*
 *主体通道类定义：
 *使用场景：发送端不是按固定的routing key发送消息，而是按字符串“匹配”发送，接收端同样如此。
 *发送消息的routing key不是固定的单词，而是匹配字符串，如"*.lu.#"，*匹配一个单词，#匹配0个或多个单词。
 */
public class TopicChannel extends AbstractChannel {
	public TopicChannel() {

	}
	
	@Override
	protected void exchangeDeclare(String exchange) throws Exception { 
		channel.exchangeDeclare(exchange, "topic"); 
		if(exchangeMap.get(exchange)==null){
			queueMap.put(exchange, true);			
		}
//		 DeclareOk ok = channel.queueDeclare(syncQueue,false,false,false, null);
//		 if(ok.getQueue().equals(syncQueue)){
//			 (new Receiver(this,syncQueue)).start();
//		 }
	}

	@Override
	public void queueBind(String exchange,String queue,boolean isDurable,String routeKey) throws Exception {       
		 channel.queueDeclare(queue,isDurable,false,false, null);
		channel.queueBind(queue,exchange, routeKey);
		if(queueMap.get(queue)==null){
			queueMap.put(queue, true);
			 (new Receiver(this,queue)).start();
		}
	}  	 
	
	@Override
	public void queueUnBind(String exchange,String queue,String routeKey) throws Exception {
		channel.queueUnbind(queue, exchange, routeKey);
		queueMap.remove(queue);
	}
}