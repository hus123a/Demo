package com.catcoder.demo.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.service.ITreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: demo
 * @description: tree接口类
 * @author: CodeCat
 * @create: 2020-05-07 23:51
 **/
@Controller()
@RequestMapping("/api/nacos")
public class NacosController {

    private static Logger logger =  LoggerFactory.getLogger(NacosController.class);

    @NacosValue(value = "${testConfig.name:2}", autoRefreshed = true)
    private String useLocalCache;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return useLocalCache;
    }

}

