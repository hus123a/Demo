package com.catCoder.watcherBuilder;

import org.springframework.stereotype.Service;

@Service
public class ThreeDealMethod implements DealMethod {
    @Override
    public void create(CreateOb createOb) {
        System.out.println(Thread.currentThread().getName()+"-- Listener for Deal Event,Three method Create Business for :" + createOb.getType());
    }
}
