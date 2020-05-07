package com.catcoder.demo.controller;

import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.mapper.TreeNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: demo
 * @description: tree接口类
 * @author: CodeCat
 * @create: 2020-05-07 23:51
 **/
@Controller
public class TreeController {

    @Autowired
    TreeNodeMapper treeNodeMapper;

    @RequestMapping("/getTree")
    @ResponseBody
    public List<MyLinkTreeNode> getTree(){

        return treeNodeMapper.selectAll();
    }

}

