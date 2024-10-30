package com.kason.mianshiya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.annotation.AuthCheck;
import com.kason.mianshiya.common.BaseResponse;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.common.ResultUtils;
import com.kason.mianshiya.constant.UserConstant;
import com.kason.mianshiya.exception.BusinessException;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankAddRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankEditRequest;
import com.kason.mianshiya.model.dto.questionbank.QuestionBankQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.entity.User;
import com.kason.mianshiya.model.vo.QuestionBankVO;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.kason.mianshiya.service.IQuestionBankService;
import com.kason.mianshiya.service.IQuestionService;
import com.kason.mianshiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private IQuestionBankQuestionService questionBankQuestionService;
    @Resource
    private IQuestionService questionService;



    @Resource
    private UserService userService;
    /**
     * 创建题库
     *
     * @param questionBankAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestionBank(@RequestBody QuestionBankAddRequest questionBankAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(questionBankAddRequest, questionBank);
        // 数据校验
        questionBankService.validQuestionBank(questionBank, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        questionBank.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionBankService.save(questionBank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBankId = questionBank.getId();
        return ResultUtils.success(newQuestionBankId);
    }


    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestionBank(@RequestBody QuestionBankQueryRequest questionBankQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankQueryRequest == null, ErrorCode.PARAMS_ERROR);

        Long bankId = questionBankQueryRequest.getId();
        ThrowUtils.throwIf(bankId == null||bankId<0, ErrorCode.PARAMS_ERROR);
        User user  = userService.getLoginUser(request);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        QuestionBank qb = questionBankService.getById((bankId));
        if(!qb.getUserId().equals(user.getId())||!userService.isAdmin(user)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }

        // 删除题库,真删除
        boolean result = questionBankService.removeById(bankId);
        // 删除题库下的题目
        questionBankQuestionService.deleteByBankId(bankId);
        return ResultUtils.success(result);
    }

    @PutMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBank(@RequestBody QuestionBankEditRequest questionBankEditRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankEditRequest == null ||questionBankEditRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(questionBankEditRequest, questionBank);
        // 数据校验
        questionBankService.validQuestionBank(questionBank, false);
        // todo 填充默认值
        QuestionBank qb = questionBankService.getById(questionBank.getId());
        ThrowUtils.throwIf(qb == null, ErrorCode.NOT_FOUND_ERROR);

        // 写入数据库

        boolean result = questionBankService.updateById(questionBank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankVO> getQuestionBankVO(QuestionBankQueryRequest questionQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Long bankId =  questionQueryRequest.getGetQuestionBankId();
        QuestionBank bank = questionBankService.getById(bankId);
        ThrowUtils.throwIf(bank == null, ErrorCode.PARAMS_ERROR);
        QuestionBankVO vo = new QuestionBankVO();
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
