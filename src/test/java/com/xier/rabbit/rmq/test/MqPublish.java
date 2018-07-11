package com.xier.rabbit.rmq.test;

import com.xier.rabbit.rmq.handler.EBus;

public class MqPublish {

    public static void main(String[] args) throws Exception {      	
		for (int i = 0; i < 100; i++) {
			PersonRecognizeLog log =new PersonRecognizeLog();
			log.setRelationOrgId(101);			
			log.setDeviceId("deviceId"+i);
			log.setAge(10);
			Long startTime = System.currentTimeMillis();
			int result = EBus.publish(log);
			Long endTime = System.currentTimeMillis();
			System.out.println("response code is:"+result+",spend:"+(endTime-startTime));
			Thread.sleep(3000);
		}
//		for (int i = 0; i < 5; i++) {
//			PersonRecognizeLog log =new PersonRecognizeLog();
//			log.setRelationOrgId(101);			
//			log.setDeviceId("deviceId"+i);
//			log.setAge(10);
//			int result = EBus.publish(log,true);
//			System.out.println("response code is:"+result);
//		}
		System.in.read();
    }
}

