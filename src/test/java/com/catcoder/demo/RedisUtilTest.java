package com.catcoder.demo;

import com.alibaba.fastjson.JSONObject;
import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.service.AddNewTreeThread;
import com.catcoder.demo.service.ITreeService;
import com.catcoder.demo.service.RollBack;
import com.catcoder.demo.utils.RedisUtil;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  {DemoApplication.class})
public class RedisUtilTest {
    private final Logger logger = LoggerFactory.getLogger(RedisUtilTest.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITreeService treeService;

    @Test
    public void testSet(){
        try{
            redisUtil.set("hello","world");
            System.out.println("设置成功！");
        }catch (Exception e){
            logger.error("设置失败！", e);
        }
    }

    @Test
    public void testSet2(){
        try{
//            redisUtil.set("hello","world");
            redisTemplate.opsForValue().set("hello","world");
            System.out.println("设置成功！");
        }catch (Exception e){
            logger.error("设置失败！", e);
        }
    }

    @Test
    public void test3(){
        List<MyLinkTreeNode> test = Lists.newArrayList();

        for (int i = 1; i< 11; i++) {
            MyLinkTreeNode temp = new MyLinkTreeNode();
            temp.setName("node"+i);
            temp.setPid(i);
            temp.setValue("节点"+i);
            temp.setLevel(i);
            test.add(temp);
        }

        addNewTrees(test);




        //线程池测试
        //ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>());
    }
    @Test
    public void test4(){
        MyLinkTreeNode temp = new MyLinkTreeNode();
        temp.setName("nodetest");
        temp.setPid(100001);
        temp.setValue("节点"+100001);
        temp.setLevel(1);
        System.out.println(treeService.addTree(temp));
    }


    //@Test
    public void addNewTrees(List<MyLinkTreeNode> treeList) throws SystemException {
        logger.info("addNewCompanyUsers 新增参保人方法");
        logger.info(">>>>>>>>>>>>参数:{}", treeList);


        if (CollectionUtils.isEmpty(treeList)) {
            logger.info(">>>>>>入参为空。");
            return;
        }


        //每条线程最小处理任务数
        int perThreadHandleCount = 1;
        //线程池的最大线程数
        int nThreads = 10;
        int taskSize = treeList.size();

        if (taskSize > nThreads * perThreadHandleCount) {
            perThreadHandleCount = taskSize % nThreads == 0 ? taskSize / nThreads : taskSize / nThreads + 1;
            nThreads = taskSize % perThreadHandleCount == 0 ? taskSize / perThreadHandleCount : taskSize / perThreadHandleCount + 1;
        } else {
            nThreads = taskSize;
        }

        logger.info("批量添加树taskSize: {}, perThreadHandleCount: {}, nThreads: {}", taskSize, perThreadHandleCount, nThreads);
        CountDownLatch mainLatch = new CountDownLatch(1);
        //监控子线程
        CountDownLatch threadLatch = new CountDownLatch(nThreads);
        //根据子线程执行结果判断是否需要回滚
        BlockingDeque<Boolean> resultList = new LinkedBlockingDeque<>(nThreads);
        //必须要使用对象，如果使用变量会造成线程之间不可共享变量值
        RollBack rollBack = new RollBack(false);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);

        List<Future<List<Object>>> futures = Lists.newArrayList();
        List<Object> returnDataList = Lists.newArrayList();
        //给每个线程分配任务
        for (int i = 0; i < nThreads; i++) {
            int lastIndex = (i + 1) * perThreadHandleCount;
            List<MyLinkTreeNode> companyUserResultVos = treeList.subList(i * perThreadHandleCount, lastIndex >= taskSize ? taskSize : lastIndex);
            AddNewTreeThread addNewCompanyUserThread = new AddNewTreeThread(mainLatch, threadLatch, rollBack, resultList, companyUserResultVos, treeService);
            Future<List<Object>> future = fixedThreadPool.submit(addNewCompanyUserThread);
            futures.add(future);
        }

        /** 存放子线程返回结果. */
        List<Boolean> backUpResult = Lists.newArrayList();
        try {
            //等待所有子线程执行完毕
            boolean await = threadLatch.await(20, TimeUnit.SECONDS);
            //如果超时，直接回滚
            if (!await) {
                rollBack.setRollBack(true);
            } else {
                logger.info("创建参保人子线程执行完毕，共 {} 个线程", nThreads);
                //查看执行情况，如果有存在需要回滚的线程，则全部回滚
                for (int i = 0; i < nThreads; i++) {
                    Boolean result = resultList.take();
                    backUpResult.add(result);
                    logger.debug("子线程返回结果result: {}", result);
                    if (result) {
                        /** 有线程执行异常，需要回滚子线程. */
                        rollBack.setRollBack(true);
                    }
                }
            }
        } catch (InterruptedException e) {
            logger.error("等待所有子线程执行完毕时，出现异常");
            throw new RuntimeException("等待所有子线程执行完毕时，出现异常，整体回滚");
        } finally {
            //子线程再次开始执行
            mainLatch.countDown();
            logger.info("关闭线程池，释放资源");
            fixedThreadPool.shutdown();
        }

        /** 检查子线程是否有异常，有异常整体回滚. */
        for (int i = 0; i < nThreads; i++) {
            if (!CollectionUtils.isEmpty(backUpResult)) {
                Boolean result = backUpResult.get(i);
                if (result) {
                    logger.info("创建参保人失败，整体回滚");
                    throw new RuntimeException("创建参保人失败");
                }
            } else {
                logger.info("创建参保人失败，整体回滚");
                throw new RuntimeException("创建参保人失败");
            }
        }

        //拼接结果
        try {
            for (Future<List<Object>> future : futures) {
                returnDataList.addAll(future.get());
            }
        } catch (Exception e) {
            logger.info("获取子线程操作结果出现异常,创建的参保人列表： {} ，异常信息： {}", e);
            throw new RuntimeException("创建参保人子线程正常创建参保人成功，主线程出现异常，回滚失败");
        }
        logger.info("全部插入成功");
    }


}