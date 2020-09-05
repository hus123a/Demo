package com.catCoder.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@Data
@TableName(value = "tree")
public class MyLinkTreeNode implements Serializable {

    private static final long serialVersionUID = -4408208426062470224L;
    private String name;
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String value;
    private Integer pid;
    private Integer level;
    @TableField(exist = false)
    private List<MyLinkTreeNode> childNodes = Collections.EMPTY_LIST;
    @TableField(exist = false)
    private MyLinkTreeNode preNode;
    @TableField(exist = false)
    private MyLinkTreeNode nextNode;
    @TableField(exist = false)
    private MyLinkTreeNode parentNode;

    public MyLinkTreeNode() {
    }

    public MyLinkTreeNode(String value) {
        this.value = value;
    }

    public MyLinkTreeNode(String name, Integer id, String value, Integer pid, Integer level) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.pid = pid;
        this.level = level;
    }

    public boolean hasChildNode(){
        return !(this.childNodes == null || this.childNodes.size() == 0);
    }

    public boolean hasParentNode(){
        return this.parentNode != null;
    }

    public static Map<String, String> getAllTreeValue(MyLinkTreeNode treeNode){
        Map<String, String> treeValueMap = new HashMap();
        List<MyLinkTreeNode> allTreeNode = getAllTreeNode(treeNode);

        for (MyLinkTreeNode each : allTreeNode) {
            treeValueMap.put(each.getName(), each.getValue());
        }
        return treeValueMap;
    }
    public static List<MyLinkTreeNode> getAllTreeNode(MyLinkTreeNode treeNode){
        List<MyLinkTreeNode> treeNodes = Collections.EMPTY_LIST;
        treeNodes.add(treeNode);
        if(treeNode.hasChildNode()){
            for (MyLinkTreeNode childNode : treeNode.getChildNodes()) {
                treeNodes.addAll(getAllTreeNode(childNode));
            }
        }
        return treeNodes;
    }
}
