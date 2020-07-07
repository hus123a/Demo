package com.catCoder.utils;

import com.catCoder.bean.FormSeq;
import com.catCoder.service.IFormSeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
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
public class IDHandler {

    @Autowired
   private IFormSeqService formSeqServiceImpl;

   private  ReentrantLock lock = new ReentrantLock();

   private IDArray<Integer> idArray = new IDArray<Integer>(500) {
       @Override
       public void addNode() {
           log.info("线程"+Thread.currentThread().getName()+"触发了扩容");
           Queue<Integer> idQuene = formSeqServiceImpl.getMultiIdQuene(new FormSeq("test"), 500);
           idQuene.forEach(id->{
               try {
                   idArray.put(id);
               } catch (InterruptedException e) {
                   log.error(e.getMessage(), e);
               }
           });
       }
   };

   public int get() throws InterruptedException {
       Integer id = null;
       lock.lockInterruptibly();
       try {
           id = this.idArray.poll(500, TimeUnit.MILLISECONDS);
       }finally {
           lock.unlock();
       }
       return id;
   }
}
