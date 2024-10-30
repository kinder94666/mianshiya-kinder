package com.kason.mianshiya.model.dto.questionbankquestion;

import com.kason.mianshiya.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
@Data
public class QuestionBankQuestionQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

}
