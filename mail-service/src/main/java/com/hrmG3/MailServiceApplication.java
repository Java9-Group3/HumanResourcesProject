package com.hrmG3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class);
    }

//    @Autowired
//    private MailSenderService mailSenderService;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail(){
//        mailSenderService.sendMail("555-555");
//    }
}