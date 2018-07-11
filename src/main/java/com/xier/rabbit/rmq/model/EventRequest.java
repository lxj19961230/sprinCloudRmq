package com.xier.rabbit.rmq.model;

public class EventRequest extends EventCommand {

	private Object eventObject;

	private Boolean isSync;

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
	}

	public Object getEventObject() {
		return eventObject;
	}

	public void setEventObject(Object eventObject) {
		this.eventObject = eventObject;
	}

}
