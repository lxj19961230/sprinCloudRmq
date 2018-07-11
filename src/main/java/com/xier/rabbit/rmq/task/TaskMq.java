package com.xier.rabbit.rmq.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.xier.rabbit.rmq.handler.EBus;
import com.xier.rabbit.rmq.mq.model.EventAnnotionListener;
import com.xier.rabbit.rmq.mq.model.TopicListener;

public class TaskMq implements InitializingBean{
	private static Logger logger = LoggerFactory.getLogger(TaskMq.class);
	@Override
	public void afterPropertiesSet() throws Exception {
		receiveMq();
	}
	
	public void receiveMq(){
		try {
			Thread thread=new Thread(()-> EBus.eventListener(new EventAnnotionListener()));
			Thread thread1=new Thread(()-> EBus.eventListener(new TopicListener()));
			thread.start();
			thread1.start();
		} catch (Exception e) {
			logger.info("启动出错啦，不过不影响！");
		}
	}
}
