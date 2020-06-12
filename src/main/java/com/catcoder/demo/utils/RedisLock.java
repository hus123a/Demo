package com.catcoder.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.CountDownLatch;

/**
 * @program: catCoder-parent
 * @description: 基于redis的分布式锁
 * @author: CodeCat
 * @create: 2020-06-12 21:48
 **/
public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static int count = 0;
    /**
     * 加锁
     * @param id
     * @return
     */
    public static int tryLock(String id, JedisPool jedisPool){
       return RedisUtil.setNxKey(id, jedisPool) ? 1 : -1;
    }

    /**
     * 释放锁
     * @param id
     * @return
     */
    public static int tryUnLock(String id, JedisPool jedisPool){
        return RedisUtil.removeNxKey(id, jedisPool)? 1 : -1;
    }

    public static void  testRedisLock(JedisPool jedisPool) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i< 10 ; i++) {
            new Thread(()->{
                String threadName = "ADD线程--"+Thread.currentThread().getName();
                try {
                    countDownLatch.await();
                    try{
                        logger.info(threadName+"开始获取锁");
                        if(RedisLock.tryLock(threadName, jedisPool) >0){
                            count ++;
                            logger.info(threadName+"累加成功！，当前count值为："+count);
                            RedisLock.tryUnLock(threadName, jedisPool);
                        }
                    }finally {
                        if(RedisLock.tryUnLock(threadName, jedisPool) > 0){
                            logger.info(threadName+"释放锁成功！");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(1000);
        countDownLatch.countDown();
    }
}

