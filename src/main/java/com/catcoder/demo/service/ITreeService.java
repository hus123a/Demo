package com.catcoder.demo.service;

import com.catcoder.demo.bean.MyLinkTreeNode;

import java.util.List;

public interface ITreeService {
    public List<MyLinkTreeNode> selectAll();

    public void addTree(MyLinkTreeNode node);
}
