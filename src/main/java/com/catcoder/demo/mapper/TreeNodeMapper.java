package com.catcoder.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catcoder.demo.bean.MyLinkTreeNode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DELL
 */
@Mapper
public interface TreeNodeMapper extends BaseMapper<TreeNodeMapper> {
    public List<MyLinkTreeNode> selectAll();

    @Insert("insert into tree (id, name, value, pid) values (#{id}, #{name},#{value}, #{pid})")
    public void addTree(MyLinkTreeNode node);
}
