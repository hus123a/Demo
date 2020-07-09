package com.catCoder.service;



import com.catCoder.bean.FormSeq;

import java.util.Queue;


public interface IFormSeqService {

    /**
     * 根据序列号code获取最新的id
     *
     * @param formSeq 必填 seqCode 流水号表示，主键为表名，业务为业务名； seqDesc 描述
     * @return int
     * @author hs
     * @date 2020/6/19 10:26
     */
    public int getId(FormSeq formSeq);
    /**
     * 用于连续的获取多个id
     *
     * @param formSeq
     * @param offset 获取id个数
     * @return int
     * @author hs
     * @date 2020/6/28 11:43
     */
    public int getMultiId(FormSeq formSeq,int offset);
    /**
     * 获取id的队列
     *
     * @param formSeq
     * @param offset
     * @return java.util.Queue<java.lang.Integer>
     * @author hs
     * @date 2020/7/2 15:14
     */
    public Queue<Integer> getMultiIdQuene(FormSeq formSeq,int offset);

    int getValue(String code);
}
