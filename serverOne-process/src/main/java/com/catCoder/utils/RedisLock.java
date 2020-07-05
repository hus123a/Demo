package com.catCoder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

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
    public static boolean tryLock(String id, JedisPool jedisPool){
       return RedisUtil.setNxKey(id, jedisPool);
    }

    /**
     * 释放锁
     * @param id
     * @return
     */
    public static boolean tryUnLock(String id, JedisPool jedisPool){
        return RedisUtil.removeNxKey(id, jedisPool);
    }

    public static void  testRedisLock(JedisPool jedisPool) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i< 300 ; i++) {
            new Thread(()->{
                Long l = 0L;
                String threadName = "ADD线程--"+Thread.currentThread().getName();
                try {
                    countDownLatch.await();
                    try{
                         l = System.currentTimeMillis();
                        if(RedisLock.tryLock(threadName, jedisPool)){
                            count ++;
                            logger.info(threadName+"累加成功！，当前count值为："+count);
                        }
                    }finally {
                        if(RedisLock.tryUnLock(threadName, jedisPool)){
                            logger.info(threadName+"获取释放锁耗时：" +(System.currentTimeMillis()-l));
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

