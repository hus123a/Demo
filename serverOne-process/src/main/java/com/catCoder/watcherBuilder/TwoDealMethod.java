package com.catCoder.watcherBuilder;

import org.springframework.stereotype.Service;

@Service
public class TwoDealMethod implements DealMethod{
    @Override
    public void create(CreateOb createOb) {
        System.out.println(Thread.currentThread().getName()+"-- Listener for Deal Event,Two method Create Business for :" + createOb.getType());
    }
}
