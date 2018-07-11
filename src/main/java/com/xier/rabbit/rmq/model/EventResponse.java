package com.xier.rabbit.rmq.model;

public class EventResponse  extends EventCommand{

     private Integer errorCode;

	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
 	
}
