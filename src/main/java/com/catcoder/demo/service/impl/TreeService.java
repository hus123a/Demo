package com.catcoder.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.catcoder.demo.annotation.redis.RedisCache;
import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.handler.MapperHandler;
import com.catcoder.demo.handler.ProxyFactory;
import com.catcoder.demo.mapper.TreeNodeMapper;
import com.catcoder.demo.service.ITreeService;
import com.catcoder.demo.service.RollBack;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TreeService implements ITreeService {

    private  final Logger logger = LoggerFactory.getLogger(TreeService.class);

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

        logger.info("走的数据库");
        return treeNodeMapper.selectOne(queryWrapper) ;
    }
}
