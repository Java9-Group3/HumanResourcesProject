package com.hrmG3.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //Mail
    @Value("${rabbitmq.mail-queue}")
    private String mailQueueName;
    @Value("${rabbitmq.mail-binding-key}")
    private String mailBindingKey;

    @Bean
    Queue mailQueue(){
        return  new Queue(mailQueueName);
    }

}
