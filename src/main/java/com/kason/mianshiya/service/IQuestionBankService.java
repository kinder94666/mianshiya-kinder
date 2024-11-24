package com.kason.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankQueryRequest;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.vo.QuestionBankVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 题库 服务类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
public interface IQuestionBankService extends IService<QuestionBank> {
    /**
     * 校验数据
     *
     * @param questionBank
     * @param add 对创建的数据进行校验
     */
    void validQuestionBank(QuestionBank questionBank, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBankQueryRequest
     * @return
     */
    QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest);

    /**
     *  获取题库封装
     * @param questionBankPage
     * @param request
     * @return
     */
    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage, HttpServletRequest request);

//    /**
//     * 获取题库封装
//     *
//     * @param questionBank
//     * @param request
//     * @return
//     */
//    QuestionBankVO getQuestionBankVO(QuestionBank questionBank, HttpServletRequest request);
//
//    /**
//     * 分页获取题库封装
//     *
//     * @param questionBankPage
//     * @param request
//     * @return
//     */
//    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage, HttpServletRequest request);
}
