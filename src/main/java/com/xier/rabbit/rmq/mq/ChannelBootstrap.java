package com.xier.rabbit.rmq.mq;
 
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.rabbitmq.client.AlreadyClosedException;
import com.xier.rabbit.rmq.handler.EventErrorCode;
import com.xier.rabbit.rmq.mq.scene.BroadcastChannel;
import com.xier.rabbit.rmq.mq.scene.RouteChannel;
import com.xier.rabbit.rmq.mq.scene.TaskChannel;
import com.xier.rabbit.rmq.mq.scene.TopicChannel;
 
public class ChannelBootstrap {
	private static Logger logger = LoggerFactory.getLogger(ChannelBootstrap.class);
	private ChannelProperty property = new ChannelProperty();
	private ConnectionOptions conn = new ConnectionOptions();
	private String serviceId;
	private AbstractChannel currentChannel;

	public ChannelBootstrap(){
		serviceId = UUID.randomUUID().toString().replace("-", "");
		conn = new ConnectionOptions();		
		try{
			Resource res = new ClassPathResource("/application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(res);
			String host = props.getProperty("spring.rabbitmq.host", "192.168.0.55");
			String port = props.getProperty("spring.rabbitmq.port", "5673");
			String userName = props.getProperty("spring.rabbitmq.userName", "guest");
			String password = props.getProperty("spring.rabbitmq.password", "lvXun@666");
	
			conn.setHost(host);
			conn.setPort(Integer.parseInt(port));
			conn.setUserName(userName);
			conn.setPassword(password);
			
			property.setRequiredType(MqType.TOPIC);
			property.setAutoAck(true);
			property.setQos(false);
			property.setDurable(true);
			
			logger.info(String.format("factoryId:%s,succeed to read mq config,host:%s,port:%s,userName:%s,password:%s",serviceId,host,port,userName,password));			
		}catch(Exception  ex){
			logger.error("serviceId:"+serviceId+",failed to read mq connection, file name :application.properties.", ex);
		} 
	}

	public ChannelBootstrap start() {
		try{
			switch (property.getRequiredType()) {
			case TASK:
				currentChannel = new TaskChannel();
				break;
			case ROUTE:
				currentChannel = new RouteChannel();
				break;
			case TOPIC:
				currentChannel = new TopicChannel();
				break;
			case BROADCAST:
				currentChannel = new BroadcastChannel();
				break;
			default:
				currentChannel = new TaskChannel();
				break;
			}
			currentChannel.createChannel(conn,property);
			Thread.sleep(1000);
			return this;
		}catch(Exception  ex){
			logger.error("serviceId:"+serviceId+",failed to read mq connection, file name :application.properties.", ex);
		} 
		return null;
	}
		
	/**
	 * 发布消息
	 * @param request
	 * @param isSync
	 * @param isDurable
	 * @return
	 */
	public int publish(Object request,boolean isSync){    
		int result = EventErrorCode.SUCCEED;
		try { 
			result = currentChannel.publish(request,isSync);
		} catch (Exception ex) {
			logger.error("failed to publish message", ex);
			result = EventErrorCode.PUBLISH_EXCEPTION;
		}
		return result;
	}
	
	/**
	 * 注册消息
	 * @param subscriber
	 * @return
	 */
	public int register(Object subscriber){ 
		try{
			currentChannel.register(subscriber);
			return EventErrorCode.SUCCEED;
		}
		catch(AlreadyClosedException ex){
			logger.error(String.format(",failed to register subscrible."),ex);
			return EventErrorCode.NETWORK_EXCEPTION;
			//this.currentChannel.isOpen=false;
		}
		catch(Exception ex){
			logger.error(String.format(",failed to register subscrible."),ex);
			return EventErrorCode.SUBSCRIBE_LISTEN_EXCEPTION;
		}
	}
	 
	public void disponse() throws Exception{
		//currentChannel.disponse();
	}
}
