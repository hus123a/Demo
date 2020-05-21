package com.catcoder.demo.service;

import com.catcoder.demo.bean.MyLinkTreeNode;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class AddNewTreeThread implements Callable<List<Object>> {
    /**
     * 主线程监控
     */
    private CountDownLatch mainLatch;
    /**
     * 子线程监控
     */
    private CountDownLatch threadLatch;

    private ITreeService treeService;
    /**
     * 是否回滚
     */
    private RollBack rollBack;
    private BlockingDeque<Boolean> resultList;
    private List<MyLinkTreeNode> taskList;

    public AddNewTreeThread(CountDownLatch mainLatch, CountDownLatch threadLatch, RollBack rollBack, BlockingDeque<Boolean> resultList, List<MyLinkTreeNode> taskList, ITreeService treeService) {
        this.mainLatch = mainLatch;
        this.threadLatch = threadLatch;
        this.rollBack = rollBack;
        this.resultList = resultList;
        this.taskList = taskList;
        this.treeService = treeService;
    }

    @Override
    public List<Object> call() {
        //为了保证事务不提交，此处只能调用一个有事务的方法，spring 中事务的颗粒度是方法，只有方法不退出，事务才不会提交
        return treeService.addNewTreeModeUsers(mainLatch, threadLatch, rollBack, resultList, taskList,treeService);
    }

}
