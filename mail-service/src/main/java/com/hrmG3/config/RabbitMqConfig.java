package com.hrmG3.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //register mail
    @Value("${rabbitmq.registerMailQueue}")
    private String registerMailQueue;

    @Bean
    Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }

    //Visitor hello mail
    @Value("${rabbitmq.registerMailHelloQueue}")
    private String registerMailHelloQueue;

    @Bean
    Queue registerMailHelloQueue(){
        return new Queue(registerMailHelloQueue);
    }

}
