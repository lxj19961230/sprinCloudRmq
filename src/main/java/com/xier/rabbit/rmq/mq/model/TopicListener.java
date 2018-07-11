package com.xier.rabbit.rmq.mq.model;

import com.xier.rabbit.rmq.annotation.Subscriber;

public class TopicListener {

    @Subscriber
    public void sub(String s) {
        System.out.println(s);
    }

}
