package com.xier.rabbit.rmq.test;

import com.xier.rabbit.rmq.annotation.Subscriber;

public class EventAnnotionListener {

    @Subscriber
    public void sub(PersonRecognizeLog eventA) {
        System.out.println(eventA.getClass() + ","+eventA.getDeviceId());
    }

}
