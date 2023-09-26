package com.hrmG3.rabbitmq.producer;


import com.hrmG3.rabbitmq.model.RegisterMailHelloModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailHelloProducer {
    @Value("${rabbitmq.exchange-auth}")
    private String directExchange;
    @Value("${rabbitmq.registerMailHelloBindingKey}")
    private String registerMailHelloBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendHello(RegisterMailHelloModel registerMailHelloModel){
        rabbitTemplate.convertAndSend(directExchange,registerMailHelloBindingKey, registerMailHelloModel);
    }
}
