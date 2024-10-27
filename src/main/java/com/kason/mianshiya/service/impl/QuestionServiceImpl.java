package com.kason.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.mapper.QuestionMapper;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.kason.mianshiya.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
