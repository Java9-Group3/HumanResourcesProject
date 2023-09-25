package com.hrmG3.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // RabbitMQ için kullanılacak olan değişkenlerin değerlerini almak için @Value kullanılır.
    @Value("${rabbitmq.user-exchange}")
    private String exchange;

    // DirectExchange bean'i oluşturur ve bu değişkeni kullanır.
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchange);
    }

    // managerChangeStatusQueue ve managerChangeStatusBindingKey değerlerini alır.
    @Value("${rabbitmq.queueManagerChangeStatus}")
    private String managerChangeStatusQueue;
    @Value("${rabbitmq.managerChangeStatusKey}")
    private String managerChangeStatusBindingKey;

    // managerChangeStatusQueue adında bir Queue bean'i oluşturur.
    @Bean
    public Queue managerChangeStatusQueue(){
        return new Queue(managerChangeStatusQueue);
    }

    // managerChangeStatusQueue ile exchange'i managerChangeStatusBindingKey ile bağlar.
    @Bean
    public Binding bindingManagerChangeStatus(final Queue managerChangeStatusQueue, final DirectExchange exchange){
        return BindingBuilder.bind(managerChangeStatusQueue).to(exchange).with(managerChangeStatusBindingKey);
    }

}
