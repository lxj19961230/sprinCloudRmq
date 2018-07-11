package com.xier.rabbit.rmq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xier.rabbit.rmq.handler.EBus;
import com.xier.rabbit.rmq.mq.model.EventAnnotionListener;
import com.xier.rabbit.rmq.mq.model.PersonRecognizeLog;

@RestController
public class MqController {

	@RequestMapping("/send")
	public void send(){
		for (int i = 0; i < 10; i++) {
			PersonRecognizeLog log =new PersonRecognizeLog();
			log.setRelationOrgId(101);			
			log.setDeviceId("deviceId"+i);
			log.setAge(10);
			Long startTime = System.currentTimeMillis();
			int result = EBus.publish(log);
			Long endTime = System.currentTimeMillis();
			System.out.println("response code is:"+result+",spend:"+(endTime-startTime));
			EBus.eventListener(new EventAnnotionListener());
		}
	}
}
