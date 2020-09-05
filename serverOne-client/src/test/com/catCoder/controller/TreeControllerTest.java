package com.catCoder.controller;

import com.catCoder.bean.MyLinkTreeNode;
import com.catCoder.bean.ObUser;
import com.catCoder.watcherBuilder.CreateOb;
import com.catCoder.watcherBuilder.DealEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.util.ObjectUtils;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TreeControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void DealEventTest(){
        ObUser user = ObUser.builder().name("阿花").age(18).sex(2).hobby("爱吃鱼").build();
        System.out.println(user);


        CreateOb createOb = new CreateOb();
        System.out.println("测试开始");
        createOb.setType("我是创建任务的动作！");
        applicationContext.publishEvent(new DealEvent(this, createOb));
    }
}