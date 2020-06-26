package com.catcoder.demo.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * @program: catCoder-parent
 * @description: fegin api实现类
 * @author: CodeCat
 * @create: 2020-06-26 21:42
 **/

@RestController
public class TestApiImpl implements TestApi{
    @Override
    public String sayHello() {
        return "hello , 这是fegin的调用 serverOne";
    }
}

