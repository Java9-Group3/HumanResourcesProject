package com.hrmG3.service;

import com.hrmG3.rabbitmq.model.*;
import com.hrmG3.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;

    public void sendMail(RegisterMailModel registerMailModel){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(registerMailModel.getEmail());
        mailMessage.setSubject("KAYDI TAMAMLAYIN LÜTFEN");
        mailMessage.setText(
                registerMailModel.getName()+" " + registerMailModel.getSurname() + " To confirm your account, please click here :\n"  +
                        "http://localhost:9090/api/v1/auth/confirm-account?token="+jwtTokenProvider.createMailToken(registerMailModel.getAuthId(), registerMailModel.getStatus()).get()
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
