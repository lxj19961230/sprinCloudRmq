package com.xier.rabbit.rmq.model;

import java.util.concurrent.CountDownLatch;

public  class EventSync {
     private CountDownLatch latch;
     private EventResponse response;
	public CountDownLatch getLatch() {
		return latch;
	}
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}
	public EventResponse getResponse() {
		return response;
	}
	public void setResponse(EventResponse response) {
		this.response = response;
	} 
	
}
