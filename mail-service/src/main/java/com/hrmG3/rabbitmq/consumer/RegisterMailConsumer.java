package com.hrmG3.rabbitmq.consumer;


import com.hrmG3.rabbitmq.model.RegisterMailModel;
import com.hrmG3.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailConsumer {
    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.registerMailQueue}")
    public void sendActivationLink(RegisterMailModel registerMailModel){
        mailService.sendMail(registerMailModel);
    }

}
