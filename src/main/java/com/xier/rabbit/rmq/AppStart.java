package com.xier.rabbit.rmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */

@EnableEurekaClient
@SpringBootApplication
public class AppStart {
	
    public static void main( String[] args )
    {
    	SpringApplication.run(AppStart.class, args);
    }
}
