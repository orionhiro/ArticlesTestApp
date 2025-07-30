package com.orionhiro.ArticlesApp.integration.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.orionhiro.ArticlesApp.service.MailService;

@SpringBootTest
@ActiveProfiles("test")
public class MailServiceTest {

    @Autowired
    private MailService mailService;
    @Value("${test.mail.to}")
    private String MAIL_TO;

    @Test
    public void mainSendTest(){
        mailService.sendMail(MAIL_TO, "Test mail", String.format("[%s] Test passed", LocalDateTime.now().toString()));
    }
}
