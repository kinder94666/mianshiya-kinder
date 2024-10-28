package com.kason.mianshiya.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.annotation.AuthCheck;
import com.kason.mianshiya.common.BaseResponse;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.common.ResultUtils;
import com.kason.mianshiya.constant.UserConstant;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankQueryRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankQuestionRemoveRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.kason.mianshiya.model.vo.QuetionBankVO;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.kason.mianshiya.service.IQuestionBankService;
import com.kason.mianshiya.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private IQuestionBankQuestionService questionBankQuestionService;
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

    /**
     * 删除题目和题库之间的联系
     * @param questionBankQuestionRemoveRequest 题目和题库id
     * @return
     */
    @PostMapping("/remove")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeQuestionBankQuestion(
            @RequestBody QuestionBankQuestionRemoveRequest questionBankQuestionRemoveRequest
    ) {
        // 参数校验
        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ErrorCode.PARAMS_ERROR);
        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
        ThrowUtils.throwIf(questionBankId == null || questionId == null, ErrorCode.PARAMS_ERROR);
        // 构造查询
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionId, questionId)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
        boolean result = questionBankQuestionService.remove(lambdaQueryWrapper);
        return ResultUtils.success(result);
    }

}
