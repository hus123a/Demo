package com.catCoder.bean;

import lombok.Data;


@Data
public class FormSeq {
    /**
     * 序列code
     */
    private String seqCode;
    /**
     * 序列值
     */
    private Integer seqValue;


    public FormSeq(){}
    public FormSeq(String seqCode){
        this.seqCode = seqCode;

    }
}
