package com.kason.mianshiya.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.common.ResultUtils;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.vo.QuetionBankVO;
import com.kason.mianshiya.common.BaseResponse;
import com.kason.mianshiya.service.IQuestionBankService;
import com.kason.mianshiya.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/questionbank")
@Slf4j
public class QuestionBankController {
    @Resource
    private IQuestionBankService questionBankService;
    @Resource
    private IQuestionService questionService;
    @GetMapping("/get/vo")
    public BaseResponse<QuetionBankVO> getQuestionBankVO(QuestionBankQueryRequest questionQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Long bankId =  questionQueryRequest.getGetQuestionBankId();
        QuestionBank bank = questionBankService.getById(bankId);
        ThrowUtils.throwIf(bank == null, ErrorCode.PARAMS_ERROR);
        QuetionBankVO vo = new QuetionBankVO();
        //是否关联查询题库下的题目列表
        boolean needQuestionList = questionQueryRequest.isNeedQuestionList();
        if (needQuestionList) {
            QuestionQueryRequest questionQueryRequest1 = new QuestionQueryRequest();
            questionQueryRequest1.setQuestionBankId(bankId);
            Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest1);
            vo.setQuestionPage(questionPage);
        }
        return ResultUtils.success(vo);

    }
}
