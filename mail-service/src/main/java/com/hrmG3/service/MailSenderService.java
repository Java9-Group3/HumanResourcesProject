package com.hrmG3.service;

import com.hrmG3.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailSenderService {


    private final JavaMailSender javaMailSender;

    // sadece kodun gönderildiği metod

//    public void sendMail(MailModel model){
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("${HRM-Mail}");
//        mailMessage.setTo(model.getEmail());
//        mailMessage.setSubject("ACTIVATION CODE");
//        mailMessage.setText("Activation code: "+model.getActivationCode());
//        javaMailSender.send(mailMessage);
//    }

    // Link+kodun gönderildiği metod
    public void sendMail(MailModel model) throws MessagingException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent("<html><body><p>Activation code: " + model.getActivationCode() + "</p><a href='https://your-company-website.com'>Link</a></body></html>", "text/html");
        helper.setTo(model.getEmail());
        helper.setSubject("ACTIVATION CODE");
        javaMailSender.send(mimeMessage);
    }

}
