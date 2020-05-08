package com.catcoder.demo.service.impl;

import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.mapper.TreeNodeMapper;
import com.catcoder.demo.service.ITreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeService implements ITreeService {

    @Autowired
    TreeNodeMapper treeNodeMapper;

    @Override
    public List<MyLinkTreeNode> selectAll() {
        return treeNodeMapper.selectAll();
    }

    @Override
    public void addTree(MyLinkTreeNode node) {
        treeNodeMapper.addTree(node);
    }
}
