package com.catCoder.utils;

import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@Data
public class jobTask  implements Callable<Object> {
    /**
     * 主线程监控
     */
    private CountDownLatch mainLatch;
    /**
     * 子线程监控
     */
    private CountDownLatch threadLatch;

    private String  name;

    public jobTask() {
    }

    public jobTask(CountDownLatch mainLatch, CountDownLatch threadLatch, String name) {
        this.mainLatch = mainLatch;
        this.threadLatch = threadLatch;
        this.name = name;
    }

    @Override
    public Object call() throws Exception {

        Object object =  this.name;




        return object;


    }
}
