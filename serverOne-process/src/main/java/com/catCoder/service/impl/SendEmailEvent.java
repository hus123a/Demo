package com.catCoder.service.impl;

import org.springframework.context.ApplicationEvent;

/*****************邮件发送事件源*************/
public class SendEmailEvent extends ApplicationEvent{



    public SendEmailEvent(Object source) {
        super(source);
    }
}

