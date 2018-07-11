package com.xier.rabbit.rmq.test;

import com.xier.rabbit.rmq.handler.EBus;

public class MqSubriable {

    public static void main(String[] args) throws Exception {      	
    	EBus.eventListener(new EventAnnotionListener());
    	
		System.in.read();
    }
}

