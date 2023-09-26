package com.hrmG3.rabbitmq.producer;

import com.hrmG3.rabbitmq.model.PersonnelPasswordModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonelPasswordProducer {
    @Value("${rabbitmq.user-exchange}")
    private String exchange;
    @Value("${rabbitmq.personnelPasswordKey}")
    private String personnelPasswordKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendPersonnelPassword(PersonnelPasswordModel model){
        System.out.println(model);
        rabbitTemplate.convertAndSend(exchange,personnelPasswordKey,model);
    }



}
