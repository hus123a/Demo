package com.catCoder.service.impl;



import com.catCoder.TargetDataSource;
import com.catCoder.bean.FormSeq;
import com.catCoder.mapper.FormSeqMapper;
import com.catCoder.service.IFormSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Queue;


@Service
public class FormSeqServiceImpl implements IFormSeqService {

    @Autowired
    FormSeqMapper formSeqMapper;

    /**
     * 根据序列号code获取最新的id
     *
     * @param formSeq 必填 seqCode 流水号表示，主键为表名，业务为业务名； seqDesc 描述
     * @return int
     * @author hs
     * @date 2020/6/19 10:26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int getId(FormSeq formSeq) {

        int update = formSeqMapper.update(formSeq);
        if(update == 0) {
            formSeq.setSeqValue(1);
            formSeqMapper.insert(formSeq);
        }
        return formSeqMapper.findValueByCode(formSeq).getSeqValue();
    }
    /**
     * 批量获取id
     *
     * @param formSeq
     * @param offset 获取id的个数
     * @return int
     * @author hs
     * @date 2020/6/28 15:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int getMultiId(FormSeq formSeq, int offset) {

        int update = formSeqMapper.updateForMulti(formSeq, offset);
        if(update == 0) {
            formSeq.setSeqValue(offset);
            formSeqMapper.insert(formSeq);
        }
        return formSeqMapper.findValueByCode(formSeq).getSeqValue();
    }
    /**
     * 获取id的队列
     *
     * @param formSeq
     * @param offset
     * @return java.util.Queue<java.lang.Integer>
     * @author hs
     * @date 2020/7/2 15:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Queue<Integer> getMultiIdQuene(FormSeq formSeq, int offset) {
        int multiId = this.getMultiId(formSeq, offset);
        Queue<Integer> ids = new ArrayDeque<>(offset);
        for (int i = multiId -  offset + 1; i<= multiId; i++) {
            ids.add(i);
        }
        return ids;
    }

    @Override
    @Transactional(readOnly = true)
    @TargetDataSource()
    public int getValue(String code) {

        return formSeqMapper.findValueByCode(new FormSeq(code)).getSeqValue();
    }
}
