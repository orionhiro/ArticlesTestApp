package com.orionhiro.ArticlesApp.listener;

import org.springframework.context.ApplicationEvent;

public class SendActivationMailEvent extends ApplicationEvent {

    public static final String MESSAGE_TEMPLATE = """
    Your ArticlesApp account is almost ready for action!
    Follow this link to complete your registration:
    http://localhost:8080/activate?code=%s
    """;
    public static final String MESSAGE_SUBJECT = "Account activation";

    public SendActivationMailEvent(Object source) {
        super(source);
    }
    
}
