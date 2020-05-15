package com.catcoder.demo;

import com.catcoder.demo.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  {DemoApplication.class})
public class RedisUtilTest {
    private final Logger logger = LoggerFactory.getLogger(RedisUtilTest.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

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

}