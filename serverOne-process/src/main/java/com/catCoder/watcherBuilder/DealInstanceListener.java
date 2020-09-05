package com.catCoder.watcherBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DealInstanceListener {
    {
        System.out.println("初始化DealInstanceListener");
    }

    @Autowired
    private DealInstanceFactory dealInstanceFactory;

    @EventListener
    @Async
    public void oneDealListener(DealEvent dealEvent){
        dealInstanceFactory.getDealMethod("one").create(dealEvent.getCreateOb());
    }
    @EventListener
    @Async
    public void twoDealListener(DealEvent dealEvent){
        dealInstanceFactory.getDealMethod("two").create(dealEvent.getCreateOb());
    }

    @EventListener
    @Async
    public void threeDealListener(DealEvent dealEvent){
        dealInstanceFactory.getDealMethod("three").create(dealEvent.getCreateOb());
    }

}
