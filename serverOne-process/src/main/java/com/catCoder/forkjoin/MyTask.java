package com.catCoder.forkjoin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * @program: catCoder-parent
 * @description: my fork/join task
 * @author: CodeCat
 * @create: 2020-09-06 17:22
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MyTask extends RecursiveTask<Long> {
    public static final long MAX = 200;
    /**
     * 子任务开始的值
     */
    private Long startValue;

    /**
     * 子任务结束的值
     */
    private Long endValue;

    @Override
    protected Long compute() {
        // 如果条件成立，说明这个任务所需要计算的数值分为足够小了
        // 可以正式进行累加计算了

        if(endValue -startValue < MAX) {
            long totalValue = 0;
            log.info("开始计算部分： startValue = {}; endValue = {}", startValue, endValue);

            for (long i = startValue; i <= endValue; i++ ){
                totalValue += i;
            }

            return totalValue;
        }
        //否则继续拆分
        else {
            MyTask subTaskPrv = new MyTask(startValue, (startValue + endValue)/2);
            subTaskPrv.fork();
            MyTask subTaskNext = new MyTask((startValue + endValue)/2 + 1, endValue);
            subTaskNext.fork();
            return subTaskPrv.join() + subTaskNext.join();
        }
    }


}

