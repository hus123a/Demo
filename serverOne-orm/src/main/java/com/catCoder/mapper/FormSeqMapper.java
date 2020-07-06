package com.catCoder.mapper;

import com.catCoder.bean.FormSeq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FormSeqMapper{

    /**
     * 根据序列code,租戶id更新序列号
     *
     * @param formSeq
     * @return int
     * @author hs
     * @date 2020/6/19 10:43
     */
    int update(FormSeq formSeq);


    /**
     * 根据序列号,租戶id查询序列号
     *
     * @param formSeq
     * @return com.minstone.form.entity.seq.FormSeq
     * @author hs
     * @date 2020/6/19 10:44
     */
    FormSeq findValueByCode(FormSeq formSeq);

    /**
     * 新增序列号
     *
     * @param formSeq
     * @return void
     * @author hs
     * @date 2020/6/19 10:44
     */
    void insert(FormSeq formSeq);

    /**
     * 批量获取id
     *
     * @param formSeq
     * @param offset 获取id的个数
     * @return int
     * @author hs
     * @date 2020/6/28 11:48
     */
    int updateForMulti(FormSeq formSeq, int offset);
}