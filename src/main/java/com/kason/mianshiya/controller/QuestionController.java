package com.kason.mianshiya.controller;

import cn.hutool.json.JSONUtil;
import com.kason.mianshiya.annotation.AuthCheck;
import com.kason.mianshiya.common.BaseResponse;
import com.kason.mianshiya.common.DeleteRequest;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.common.ResultUtils;
import com.kason.mianshiya.constant.UserConstant;
import com.kason.mianshiya.exception.BusinessException;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.model.dto.question.QuestionAddRequest;
import com.kason.mianshiya.model.dto.question.QuestionBatchDeleteRequest;
import com.kason.mianshiya.model.dto.question.QuestionUpdateRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.User;
import com.kason.mianshiya.model.vo.QuestionVO;
import com.kason.mianshiya.service.IQuestionService;
import com.kason.mianshiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    @Resource
    private IQuestionService questionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题目
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        questionService.validQuestion(question, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除题目
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目（仅管理员可用）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        questionService.validQuestion(question, false);
        // 判断题目是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 检测和处置爬虫（可以自行扩展为 - 登录后才能获取到答案）
        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            crawlerDetect(loginUser.getId());
//        }
        // 友情提示，对于敏感的内容，可以再打印一些日志，记录用户访问的内容
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 批量删除题目
     * @param questionBatchDeleteRequest 批量删除请求
     * @param request 请求
     * @return
     */
    @PostMapping("/delete/batch")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> batchDeleteQuestions(@RequestBody QuestionBatchDeleteRequest questionBatchDeleteRequest,
                                                      HttpServletRequest request) {
        ThrowUtils.throwIf(questionBatchDeleteRequest == null, ErrorCode.PARAMS_ERROR);
        questionService.batchDeleteQuestions(questionBatchDeleteRequest.getQuestionIdList());
        return ResultUtils.success(true);
    }


}
