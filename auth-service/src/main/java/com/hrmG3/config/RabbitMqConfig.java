package com.hrmG3.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;

    @Bean
    DirectExchange directExchange() {return new DirectExchange(exchange);}


    //Mail işlemi için
    @Value("${rabbitmq.registerMailQueue}")
    private String registerMailQueue;
    @Value("${rabbitmq.registerMailBindingKey}")
    private String registerMailBindingKey;

    @Bean
    Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }

    @Bean
    public Binding bindingRegisterMail(final Queue registerMailQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBindingKey);
    }

    //Visitor Hello

    @Value("${rabbitmq.registerMailHelloQueue}")
    private String registerMailHelloQueue;
    @Value("${rabbitmq.registerMailHelloBindingKey}")
    private String registerMailHelloBindingKey;

    @Bean
    Queue registerMailHelloQueue(){
        return new Queue(registerMailHelloQueue);
    }

    @Bean
    public Binding bindingRegisterHelloMail(final Queue registerMailHelloQueue, final DirectExchange exchangeAuthHello){
        return BindingBuilder.bind(registerMailHelloQueue).to(exchangeAuthHello).with(registerMailHelloBindingKey);
    }




}
