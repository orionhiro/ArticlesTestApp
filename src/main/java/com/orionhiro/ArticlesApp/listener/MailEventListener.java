package com.orionhiro.ArticlesApp.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.service.MailService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MailEventListener {

    private MailService mailService;

    @EventListener
    public void acceptEntity(SendActivationMailEvent event){ 

        User user = (User)event.getSource();
        mailService.sendMail(
            user.getEmail(), 
            SendActivationMailEvent.MESSAGE_SUBJECT, 
            String.format(SendActivationMailEvent.MESSAGE_TEMPLATE, user.getActivationCode())
        );
    }
}
