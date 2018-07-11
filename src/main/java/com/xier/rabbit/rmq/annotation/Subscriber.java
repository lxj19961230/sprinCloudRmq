package com.xier.rabbit.rmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
* 注解方式的订阅者
* @author  陈智帅   创建时间：2016年11月7日
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {
   
}
