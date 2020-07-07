package com.catCoder.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catCoder.bean.MyLinkTreeNode;
import com.catCoder.handler.ProxyFactory;
import com.catCoder.mapper.TreeNodeMapper;
import com.catCoder.service.ITreeService;
import com.catCoder.service.RollBack;
import com.catCoder.utils.IDHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class TreeService implements ITreeService {

    private  final Logger logger = LoggerFactory.getLogger(TreeService.class);

    private ConcurrentLinkedDeque<Integer> ids = new ConcurrentLinkedDeque<>();

    @Autowired
    TreeNodeMapper treeNodeMapper;

    @Override
    public List<MyLinkTreeNode> selectAll() {
        return treeNodeMapper.selectAll();
    }

    @Override
    @Transactional
    public boolean addTree(MyLinkTreeNode node) {
        return treeNodeMapper.addTree(node);
    }
    
    private static final int THREAD_NUM = 8888;



    private static long startTime = 0L;
    
    @Autowired
    private IDHandler idHandler;
            

    @Override
    @Transactional
    public List<Object> addNewTreeModeUsers(CountDownLatch mainLatch, CountDownLatch threadLatch, RollBack rollBack, BlockingDeque<Boolean> resultList, List<MyLinkTreeNode> taskList, ITreeService treeService) {
        List<Object> returnDataList = Lists.newArrayList();
        Boolean result = false;
        logger.debug("线程: {}创建参保人条数 : {}", Thread.currentThread().getName(), taskList.size());
        try {
            int i = 1;
            for (MyLinkTreeNode myLinkTreeNode : taskList) {
                logger.info("子线程 {} 运行第"+i+"次插入", Thread.currentThread().getName());
//                if(myLinkTreeNode.getLevel() == 5){
//                    int j = 1/0;
//                }
                returnDataList.add(treeService.addTree(myLinkTreeNode));
            }
            //Exception 和 Error 都需要抓
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.info("线程: {}创建参保人出现异常： {} ", Thread.currentThread().getName(), throwable);
            result = true;
        }

        resultList.add(result);
        threadLatch.countDown();
        logger.info("子线程 {} 计算过程已经结束，等待主线程通知是否需要回滚", Thread.currentThread().getName());

        try {
            mainLatch.await();
            logger.info("子线程 {} 再次启动", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            logger.error("批量创建参保人线程InterruptedException异常");
            throw new RuntimeException("批量创建参保人线程InterruptedException异常");
        }

        if (rollBack.getRollBack()) {
            logger.error("批量创建参保人线程回滚, 线程: {}, 需要更新的信息taskList: {}",
                    Thread.currentThread().getName(),
                    JSONObject.toJSONString(taskList));
            logger.info("子线程 {} 执行完毕，线程退出", Thread.currentThread().getName());
            throw new RuntimeException("批量创建参保人线程回滚");
        }

        logger.info("子线程 {} 执行完毕，线程退出", Thread.currentThread().getName());
        return returnDataList;
    }

    @Override
    //@RedisCache
    public MyLinkTreeNode selectOne(MyLinkTreeNode node) {
        QueryWrapper<MyLinkTreeNode> queryWrapper = new QueryWrapper<MyLinkTreeNode>();
        queryWrapper.eq("id",node.getId());

        treeNodeMapper = ProxyFactory.getProxyInstance(treeNodeMapper, treeNodeMapper.getClass().getInterfaces()[0]);
        System.out.println(treeNodeMapper.getClass().getInterfaces()[0].getClassLoader());
        System.out.println(treeNodeMapper.getClass().getClassLoader());
        logger.info("走的数据库");
        return treeNodeMapper.selectOne(queryWrapper) ;
    }

    @Override
    public void testGetId() {
        try {
            ids.clear();
            startTime = System.currentTimeMillis();
            log.info("CountDownLatch started at: " + startTime);
            // 初始化计数器为1
            CountDownLatch countDownLatch = new CountDownLatch(1);
            CountDownLatch c = new CountDownLatch(THREAD_NUM);

            for (int i = 0; i < THREAD_NUM; i ++) {
                new Thread(new Run(countDownLatch, idHandler, ids, c)).start();
            }
            // 启动多个线程
            countDownLatch.countDown();

            //所有线程执行完成之后
            c.await();

            //如果ids.size() == THREAD_NUM 说明全部没问题

           if(ids.size() == THREAD_NUM){
               log.error("全部获取到了id");
           }else {
               log.error("ids大小为："+ ids.size()+"存在"+(THREAD_NUM-ids.size())+"个没有获取到id的线程");
           }


        } catch (Exception e) {
            log.error("Exception: " + e.getMessage(),e);
        }
    }

    private static class Run implements Runnable {
        private CountDownLatch startLatch;

        private CountDownLatch end;

        private IDHandler idHandler;

        private ConcurrentLinkedDeque<Integer> ids;

        public Run(CountDownLatch startLatch, IDHandler idHandler, ConcurrentLinkedDeque<Integer> ids, CountDownLatch end) {
            this.startLatch = startLatch;
            this.idHandler = idHandler;
            this.ids = ids;
            this.end =  end;
        }

        @Override
        public void run() {
            try {
                // 线程等待
                startLatch.await();
                int i = idHandler.get();
                long endTime = System.currentTimeMillis();
                //log.info("线程"+Thread.currentThread().getName()+"获取了id :"+ i+ ", cost: " + (endTime - System.currentTimeMillis()) + " ms.");ms
                if(ids.contains(i)){
                    log.info("存在获取重复的id:"+ i+",当前线程为"+ Thread.currentThread().getName());
                }
                ids.add(i);

            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }finally {
                end.countDown();
            }
        }
    }
}
