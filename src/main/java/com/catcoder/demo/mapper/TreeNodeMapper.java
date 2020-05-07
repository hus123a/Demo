package com.catcoder.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catcoder.demo.bean.MyLinkTreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface TreeNodeMapper extends BaseMapper<TreeNodeMapper> {
    public List<MyLinkTreeNode> selectAll();

}
