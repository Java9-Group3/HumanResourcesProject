package com.hrmG3.rabbitmq.consumer;

import com.hrmG3.rabbitmq.model.RegisterMailHelloModel;
import com.hrmG3.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailHelloConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.registerMailHelloQueue}")
    public void sendHello(RegisterMailHelloModel registerMailHelloModel){
        mailService.sendHelloMail(registerMailHelloModel);
    }
}
