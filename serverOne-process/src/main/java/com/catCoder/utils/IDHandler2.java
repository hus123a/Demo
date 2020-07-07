package com.catCoder.utils;

import com.catCoder.bean.FormSeq;
import com.catCoder.service.IFormSeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TODO
 *
 * @author hs
 * @date 2020/7/6 17:24
 * Copyright 广州明动软件股份有限公司
 */
@Component
@Slf4j
public class IDHandler2 {

   @Autowired
   private IFormSeqService formSeqServiceImpl;

   private  ReentrantLock lock = new ReentrantLock();

   private ArrayBlockingQueue<Integer> idArray = new ArrayBlockingQueue<>(500);

   private ConcurrentHashMap<String, ArrayBlockingQueue<Integer>> idMaps = new ConcurrentHashMap<>();

   public Integer get(long timeout, String code) throws InterruptedException {
       Integer id = null;
       lock.lockInterruptibly();
       long start = System.currentTimeMillis();
       boolean flag = true;
       try {
            ArrayBlockingQueue<Integer> temp = this.idMaps.get(code);
           if(temp == null){
               log.info("初始化"+code+"的id容器");
               temp = new  ArrayBlockingQueue<>(500);
               idMaps.put(code,temp);
           }else {
               log.info("已经存在"+code+"容器，直接获取");
           }

           while ((id = temp.poll()) == null) {
               if (System.currentTimeMillis() - start >= timeout){
                   return null;
               }
               if(flag){
                   flag = false;
                   new Thread(()->{
                       addNode(code);
                   }).start();
               }
           }
           return id;
       } finally {
           lock.unlock();
       }
   }

    public void addNode(String code) {
        log.info("线程"+Thread.currentThread().getName()+"触发了扩容");
        Queue<Integer> idQuene = formSeqServiceImpl.getMultiIdQuene(new FormSeq("test"), 500);
        ArrayBlockingQueue<Integer> codes = idMaps.get(code);
        idQuene.forEach(id->{
            try {
                codes.put(id);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
