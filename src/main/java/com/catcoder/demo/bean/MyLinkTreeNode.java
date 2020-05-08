package com.catcoder.demo.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@Data
public class MyLinkTreeNode {
    private String name;
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String value;
    private Integer pid;
    private List<MyLinkTreeNode> childNodes = Collections.EMPTY_LIST;

    private MyLinkTreeNode preNode;

    private MyLinkTreeNode nextNode;

    private MyLinkTreeNode parentNode;

    public MyLinkTreeNode() {
    }

    public MyLinkTreeNode(String value) {
        this.value = value;
    }

    public MyLinkTreeNode(String name, Integer id, String value, Integer pid) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.pid = pid;
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
