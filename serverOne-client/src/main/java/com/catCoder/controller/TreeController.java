package com.catCoder.controller;

import com.catCoder.bean.MyLinkTreeNode;
import com.catCoder.service.ITreeService;
import com.catCoder.utils.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @program: demo
 * @description: tree接口类
 * @author: CodeCat
 * @create: 2020-05-07 23:51
 **/
@Controller()
@RequestMapping("/api/tree")
public class TreeController {

    private static Logger logger =  LoggerFactory.getLogger(TreeController.class);
    @Autowired
    private ITreeService treeService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/getTree", method = RequestMethod.GET)
    @ResponseBody
    public List<MyLinkTreeNode> getTree(){

        return treeService.selectAll();
    }
    @RequestMapping(value = "/addTree", method = RequestMethod.POST)
    @ResponseBody
    public String addTree(MyLinkTreeNode treeNode){
        String msg = "失败";
        try {
            treeService.addTree(treeNode);
            msg = "成功";
        }catch (Exception e) {
            logger.error(msg,e);
        }
        return msg;
    }

    @RequestMapping(value = "/getTreeById", method = RequestMethod.POST)
    @ResponseBody
    public MyLinkTreeNode getTreeById(@RequestParam(required = true) int id){
        MyLinkTreeNode myLinkTreeNode = new MyLinkTreeNode();
        myLinkTreeNode.setId(id);
        return treeService.selectOne(myLinkTreeNode);
    }


    @RequestMapping(value = "/testRedisLock", method = RequestMethod.GET)
    @ResponseBody
    public void testRedisLock() throws InterruptedException {
        RedisLock.testRedisLock(jedisPool);
    }

    @RequestMapping(value = "/testIDHandler", method = RequestMethod.GET)
    @ResponseBody
    public void testIDHandler() throws InterruptedException {
        treeService.testGetId();
    }
}

