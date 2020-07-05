package com.catCoder.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catCoder.bean.MyLinkTreeNode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author DELL
 */
@Mapper
public interface TreeNodeMapper extends BaseMapper<MyLinkTreeNode> {
    public List<MyLinkTreeNode> selectAll();

    @Insert("insert into tree (id, name, value, pid, level) values (#{id}, #{name},#{value}, #{pid}, #{level})")
    public boolean addTree(MyLinkTreeNode node);


}
