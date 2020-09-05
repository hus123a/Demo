package com.catCoder.watcherBuilder;

import org.springframework.stereotype.Service;

@Service
public class OneDealMethod implements DealMethod {
    @Override
    public void create(CreateOb createOb) {
        System.out.println(Thread.currentThread().getName()+"-- Listener for Deal Event,One method Create Business for :" + createOb.getType());
    }
}
