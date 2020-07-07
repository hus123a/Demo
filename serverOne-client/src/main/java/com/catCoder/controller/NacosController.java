package com.catCoder.controller;


import com.catCoder.api.ServerTwoClient;
import com.catCoder.bean.FormSeq;
import com.catCoder.service.IFormSeqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: demo
 * @description: tree接口类
 * @author: CodeCat
 * @create: 2020-05-07 23:51
 **/
@Controller
@RefreshScope
@RequestMapping("/api/nacos")
public class NacosController {

    private static Logger logger =  LoggerFactory.getLogger(NacosController.class);

    @Value(value = "${testConfig:2}")
    private String useLocalCache;

    @Value(value = "${testConfig.name:2}")
    private String useLocalCache1;

    @Value("${spring.datasource.url:没有}")
    private String url;

    @Autowired
    private ServerTwoClient serverTwoClientImpl;

    @Autowired
    private IFormSeqService formSeqServiceImpl;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return useLocalCache;
    }

    @RequestMapping(value = "/get1", method = RequestMethod.GET)
    @ResponseBody
    public String get1() {
        return useLocalCache1;
    }

    @RequestMapping(value = "/get2", method = RequestMethod.GET)
    @ResponseBody
    public String get2() {
        return url;
    }

    @RequestMapping(value = "/getforRemote", method = RequestMethod.POST)
    @ResponseBody
    public String getforRemote() {
        return useLocalCache;
    }

    @RequestMapping(value = "/testFegin", method = RequestMethod.GET)
    @ResponseBody
    public String testFegin() {
        return serverTwoClientImpl.sayHello();
    }

    @RequestMapping(value = "/getSeq", method = RequestMethod.GET)
    @ResponseBody
    public String getSeq() {
        return formSeqServiceImpl.getId(new FormSeq("test"))+"";
    }

    public void testGetId(){

    }

}

