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

   private int limitSize = 100;

   public Integer get(long timeout, String code) throws InterruptedException {
       Integer id = null;
       lock.lockInterruptibly();
       try {
           long start = System.currentTimeMillis();
           boolean flag = true;
            ArrayBlockingQueue<Integer> temp = this.idMaps.get(code);
           if(temp == null){
               temp = new  ArrayBlockingQueue<>(500);
               idMaps.put(code,temp);
           }
           while ((id = temp.poll()) == null) {
               if (System.currentTimeMillis() - start >= timeout){
                   log.warn("线程"+Thread.currentThread().getName()+"获取id超时");
                   return null;
               }
               if(flag){
                   flag = false;
                   addNode(code);
               }
           }
           return id;
       } finally {
           lock.unlock();
       }
   }

    public void addNode(String code) {

        long l = System.currentTimeMillis();
        Queue<Integer> idQuene = formSeqServiceImpl.getMultiIdQuene(new FormSeq(code), 500);
        try{
            log.info("线程"+Thread.currentThread().getName()+"触发了扩容,cost："+(System.currentTimeMillis() - l)+ "ms");
        }catch (Exception e){
            log.warn("sql执行异常, 线程"+Thread.currentThread().getName()+"扩容失败！");
        }
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
