package com.catCoder.forkjoin;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @program: catCoder-parent
 * @description: fork/join test
 * @author: CodeCat
 * @create: 2020-09-06 17:21
 **/
@Slf4j
public class MyForkJoinPoolTest {



    public static void main(String[] args) {
       /* ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> tashFuture = forkJoinPool.submit(new MyTask(1L, 100000002L));
        try {
            long l = System.currentTimeMillis();
            Long result = tashFuture.get();

            log.info("fork/join 执行的 result = {}, 执行耗时：{} ms", result, (System.currentTimeMillis() - l));

        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }finally {
            if(!forkJoinPool.isShutdown()){
                forkJoinPool.shutdown();
            }
        }*/
        long l = System.currentTimeMillis();
        long total = 0;
        for (long i = 1L; i<1000000002L; i++) {
            total += i;
        }
        log.info("total = {}, 执行耗时：{} ms", total, (System.currentTimeMillis() - l));



    }
}

