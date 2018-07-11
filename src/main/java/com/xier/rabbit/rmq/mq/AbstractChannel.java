package com.xier.rabbit.rmq.mq;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.xier.rabbit.rmq.annotation.Subscriber;
import com.xier.rabbit.rmq.handler.EventErrorCode;
import com.xier.rabbit.rmq.handler.EventHandler;
import com.xier.rabbit.rmq.handler.TypeConvert;
import com.xier.rabbit.rmq.model.EventRequest;
import com.xier.rabbit.rmq.model.EventResponse;
import com.xier.rabbit.rmq.model.EventSync;

/*
 * 抽象通道类定义：
 * 目前有四类场景：任务、路由、发布/订阅及主题模式.
 * 说明：
 */
public abstract class AbstractChannel  {
	private static Logger logger = LoggerFactory.getLogger(AbstractChannel.class);

	private final String subscribeLogFormat="subscribe message,msgId:%s,errorCod:%d,class:%s.";
	private final String responseLogFormat="response message,msgId:%s,errorCod:%d.";
	private final String requestLogFormat="publish message,msgId:%s,class:%s.";
	protected ConnectionOptions connectionOpts;
	private Connection connection;
	private ConnectionFactory factory;
	private final int timeOut=10;	
	protected final String syncQueue=".sync";
	protected Channel channel;
	protected ChannelProperty property;
	protected boolean isOpen = true;
	private Object syncObj=new Object();
	private Map<String, Object> listenerMap = new ConcurrentHashMap<String, Object>();
	private Map<Class<?>, EventHandler> methodsListener = new HashMap<Class<?>, EventHandler>();
	private Map<String, EventSync> syncMap = new ConcurrentHashMap<String, EventSync>();
	protected Map<String, Boolean> queueMap = new ConcurrentHashMap<String, Boolean>();
	protected Map<String, Boolean> exchangeMap = new ConcurrentHashMap<String, Boolean>();
 
	public Channel createChannel(ConnectionOptions connOpt, ChannelProperty property) throws Exception {
		this.connectionOpts = connOpt;
		factory = new ConnectionFactory();
		factory.setHost(connOpt.getHost());
		factory.setUsername(connOpt.getUserName());
		factory.setPassword(connOpt.getPassword());
		factory.setRequestedHeartbeat(5);
		//implements ExceptionHandler
		//factory.setExceptionHandler(this);
		if (connOpt.getPort() > 0) {
			factory.setPort(connOpt.getPort());
		}
		connectionHandler();
		this.property = property;
		Thread thread = new ChannelKeepalive(this);
		thread.start();
		return channel;
	}

	protected abstract void exchangeDeclare(String exchange) throws Exception;

	public abstract void queueBind(String exchange,String queue,boolean isDurable,String routeKey) throws Exception;

	public abstract void queueUnBind(String exchange,String queue,String routeKey) throws Exception;
	
	public int publish(Object request,boolean isSync) throws Exception{
        if(channel==null){
			this.isOpen=false;
        	throw (new Exception("网络异常"));
        }
		// 是否设置消息持久化
		int result=EventErrorCode.SUCCEED;
		BasicProperties props = null;
		String routeKey = request.getClass().getName();
		String exchange = routeKey; 
		try{
			this.exchangeDeclare(exchange);
		}catch(AlreadyClosedException ex){
			this.isOpen=false;
			throw ex;
		}

//		if (isDurable) {
//			props = MessageProperties.PERSISTENT_TEXT_PLAIN;
//		}
		EventRequest command = new EventRequest();
		String msgId = UUID.randomUUID().toString().replace("-", "");
		command.setMsgId(msgId);
		command.setIsSync(isSync);
		command.setEventObject(request);
		logger.debug(String.format(requestLogFormat, msgId,request));
		byte[] buffer = TypeConvert.Object2ByteArray(command);
		if(isSync){
			//订阅同步主题
			this.queueBind(exchange,exchange+syncQueue,false,msgId);
			EventSync eventSync = new EventSync();
			CountDownLatch latch = new CountDownLatch(1);
			eventSync.setLatch(latch);
			this.syncMap.put(msgId, eventSync);
  
			channel.basicPublish(exchange, routeKey, props, buffer);		 
 
			//处理同步主题
			boolean awaitResult =  latch.await(timeOut,TimeUnit.SECONDS);
			if(awaitResult){
				EventResponse response = eventSync.getResponse();
				result =(response==null?EventErrorCode.SUBSCRIBE_EXCEPTION:response.getErrorCode()); 
			}else{
				result = EventErrorCode.PUBLISH_TIMEOUT;
			}
			this.queueUnBind(exchange,exchange+syncQueue,msgId);
			syncMap.remove(msgId);
		}else{
				channel.basicPublish(exchange, routeKey, props, buffer);
		}
		return result;
	}
	
	private void publishSyncResponse(String exchange,String routeKey,EventResponse response) throws Exception{
		BasicProperties props = null; 
		byte[] buffer = TypeConvert.Object2ByteArray(response);
		channel.basicPublish(exchange, routeKey, null, buffer);	
	}
	
	private void setHandler(Object listener) {
		EventHandler handler = null;
		Class clazz = listener.getClass();
		String listenerName=clazz.getName();
		if(this.listenerMap.get(listenerName)==null){
			this.listenerMap.put(listenerName, listener);
			logger.info("succeed to register event:"+listenerName);
		}
		while (clazz != null) {
			for (Method method : clazz.getMethods()) {
				Subscriber annotation = method.getAnnotation(Subscriber.class);
				if (annotation != null) {
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length != 1) {
						throw new IllegalArgumentException(
								"Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length
										+ " arguments.  Event handler methods " + "must require a single argument.");
					}
					Class<?> eventType = parameterTypes[0];
					String eventString = eventType.getName();
					try {
						if (methodsListener.get(eventType) == null) {
							handler = new EventHandler(listener, method);
							methodsListener.put(eventType, handler);
							this.exchangeDeclare(eventString);
							this.queueBind(eventString,listenerName,false,eventString);
						}
					} catch (Exception ex) {
                            logger.warn("failed to add listener,eventType:"+eventType,ex);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private void startListener(String queue) throws Exception {
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queue, true, consumer);
		if (property.isQos()) {
			channel.basicQos(1);// 设置平均分配
		}
		while (true) {
			QueueingConsumer.Delivery delivery = null;
			try {
				delivery = consumer.nextDelivery();
			} catch (Exception ex) {
				this.isOpen = false;
				logger.warn("there have an exception while waiting.", ex);
				break;
			}
			EventResponse response = new EventResponse();
			EventRequest request=null;
			Object object=null;
			try {
				//Object object = TypeConvert.byteArray2Object(delivery.getBody());
				Object command =TypeConvert.byteArray2Object(delivery.getBody());
				if (command instanceof EventResponse) {
					//TODO这里执行响应
					EventResponse eventResponse =(EventResponse)command;
					logger.debug(String.format(responseLogFormat, eventResponse.getMsgId(),eventResponse.getErrorCode(),""));
					EventSync eventSync = this.syncMap.get(eventResponse.getMsgId());
					if(eventSync!=null){
						eventSync.setResponse(eventResponse);
						eventSync.getLatch().countDown();
					}
					continue;
				}else if (command instanceof EventRequest) {
					 request =(EventRequest)command;
					 response.setMsgId(request.getMsgId());
					object = request.getEventObject();
					EventHandler handler = methodsListener.get(object.getClass());
					handler.handleEvent(object);
					response.setErrorCode(EventErrorCode.SUCCEED);
					logger.debug(String.format(subscribeLogFormat, request.getMsgId(),response.getErrorCode(),object));
					//发送响主题	
					if(request!=null){
						if(request.getIsSync()){
							this.publishSyncResponse(object.getClass().getName(),response.getMsgId(), response);
						}
					}
				}else{
					logger.warn(String.format(subscribeLogFormat, "",EventErrorCode.INVALID_MESSAGE,object));
				}

			} catch (Exception ex) {
				response.setErrorCode(EventErrorCode.SUBSCRIBE_EXCEPTION);
				if(request!=null){
					logger.warn(String.format(subscribeLogFormat, request.getMsgId(),response.getErrorCode(),object));
				}
			}finally{
				if (!property.isAutoAck()) {
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				}
			}
		}
	}

	public boolean register(Object subscriber) throws Exception {
		if(this.channel==null){
			return false;
		}
		try{
			setHandler(subscriber);	
			return true;
		}catch(Exception ex){
			logger.warn("failed to register subscriber.",ex);
			return false;
		}
	}

	public void disponse() throws Exception {
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	private void connectionHandler() {
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			isOpen = true;		
			logger.info("succeed to connect rabbitmq server.");
		} catch (Exception ex) {
			isOpen = false;
			logger.error("failed to connect rabbitmq server.", ex);
		}
	}
	
	private void keepaliveHandler() {
			try {
				connectionHandler();
				if(isOpen){
					methodsListener.clear();
					queueMap.clear();
					syncMap.clear();
					exchangeMap.clear();
					if(this.listenerMap!=null){
						for (Map.Entry<String, Object> entry : listenerMap.entrySet()) {  
							  this.register(entry.getValue());
						}  
					}				
				}
			} catch (Exception ex) {
				isOpen = false;
				logger.error("failed to connect rabbitmq server.", ex);
			}
	}
 
	protected class Receiver extends Thread {
		private AbstractChannel instance;
        private String queue;
		public Receiver(AbstractChannel channel,String queue) {
			this.instance = channel;
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				instance.startListener(queue);
			} catch (Exception ex) {
				logger.error(String.format("failed to start listening  message."), ex);
			}
		}
	}

	class ChannelKeepalive extends Thread {

		private AbstractChannel instance;

		public ChannelKeepalive(AbstractChannel channel) {
			this.instance = channel;
		}

		@Override
		public void run() {
			while (true) {
				synchronized(syncObj){
					try {
						Thread.sleep(3000);
							if (instance.isOpen == false) {
								instance.keepaliveHandler();
							}
						} catch (Exception ex) {
                               logger.error("keepaliveHandler throws Exception.",ex);
						}
				}

			}
		}
	}
}
