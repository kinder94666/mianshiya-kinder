package com.kason.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.constant.QuestionXBankConstant;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.mapper.QuestionMapper;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.kason.mianshiya.model.entity.User;
import com.kason.mianshiya.model.vo.QuestionVO;
import com.kason.mianshiya.model.vo.UserVO;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.kason.mianshiya.service.IQuestionService;
import com.kason.mianshiya.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Resource
    private IQuestionBankQuestionService questionBankService;
    @Resource
    private UserService userService;
    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest) {

        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();

        LambdaQueryChainWrapper<Question> queryWrapper = this.lambdaQuery();
        Long questionBankId = questionQueryRequest.getQuestionBankId();
        if(questionBankId != null){
            // 查询指定题库的题目
            LambdaQueryWrapper<QuestionBankQuestion> questionBankQuestionLambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .select(QuestionBankQuestion::getQuestionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            List<QuestionBankQuestion> questionList = questionBankService.list(questionBankQuestionLambdaQueryWrapper);
            if(CollUtil.isNotEmpty(questionList)){
                Set<Long> collect = questionList.stream()
                        .map(QuestionBankQuestion::getQuestionId)
                        .collect(Collectors.toSet());
                queryWrapper.in(Question::getId, collect);
            }
        }

        Page<Question> page = this.page(new Page<>(current, size), queryWrapper);

        return page;
    }

    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        String title = question.getTitle();
        String content = question.getContent();

        if(add){
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        //检查字段长度
        ThrowUtils.throwIf(title.length() > QuestionXBankConstant.TITLE_MAXLEN, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(content.length() > QuestionXBankConstant.CONTENT_MAXLEN, ErrorCode.PARAMS_ERROR);

    }
    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.objToVo(question);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUser(userVO);
        // endregion
        return questionVO;
    }




}
