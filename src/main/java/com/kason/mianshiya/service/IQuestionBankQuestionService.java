package com.kason.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kason.mianshiya.model.entity.User;
import com.kason.mianshiya.model.vo.QuestionBankQuestionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 题库题目 服务类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
public interface IQuestionBankQuestionService extends IService<QuestionBankQuestion> {
    void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion,boolean add);

    void deleteByBankId(Long bankId);

    QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest);

    Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request);

    QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request);

    void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, User loginUser);

    void batchRemoveQuestionsFromBank(List<Long> questionIdList, Long questionBankId);
}
