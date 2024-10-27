package com.kason.mianshiya.service;

import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 题库题目 服务类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
public interface IQuestionBankQuestionService extends IService<QuestionBankQuestion> {
    public void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion,boolean add);
}
