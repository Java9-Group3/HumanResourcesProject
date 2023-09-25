package com.hrmG3.service;

import com.hrmG3.rabbitmq.model.RegisterMailHelloModel;
import com.hrmG3.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(RegisterMailModel registerMailModel){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
       mailMessage.setFrom("${spring.mail.username}");

        mailMessage.setTo(registerMailModel.getEmail());
        mailMessage.setSubject("KAYDI TAMAMLAYIN LÜTFEN");
        mailMessage.setText(
                "sa"
        );
        javaMailSender.send(mailMessage);
    }


    public void sendHelloMail(RegisterMailHelloModel registerMailHelloModel) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(registerMailHelloModel.getEmail());
        mailMessage.setSubject("AKTIVASYON KODU");
        mailMessage.setText(
                "Hello visitor, " +  registerMailHelloModel.getName() +" "+ registerMailHelloModel.getSurname()+" you have successfully registered\n"
                        +   "email: " + registerMailHelloModel.getEmail()

        );
        javaMailSender.send(mailMessage);
    }


}
