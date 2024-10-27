package com.kason.mianshiya.service.impl;

import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.kason.mianshiya.mapper.QuestionBankQuestionMapper;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kason.mianshiya.service.IQuestionBankService;
import com.kason.mianshiya.service.IQuestionService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 题库题目 服务实现类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
@Service
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements IQuestionBankQuestionService {

    @Resource
    @Lazy
    private IQuestionService questionService;

    @Resource
    private IQuestionBankService questionBankService;


    @Override
    public void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add) {
        ThrowUtils.throwIf(null == questionBankQuestion.getQuestionBankId(), ErrorCode.PARAMS_ERROR);
        if(questionBankQuestion.getQuestionId() != null){
            Question question = questionService.getById(questionBankQuestion.getQuestionId());
            ThrowUtils.throwIf(question==null, ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        if(questionBankQuestion.getQuestionBankId()!=null){

            QuestionBank questionBank = questionBankService.getById(questionBankQuestion.getQuestionBankId());
            ThrowUtils.throwIf(questionBank==null, ErrorCode.NOT_FOUND_ERROR,"题库不存在");
        }
    }
}
