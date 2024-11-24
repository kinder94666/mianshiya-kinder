package com.kason.mianshiya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kason.mianshiya.model.dto.question.QuestionQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 题目 服务类
 * </p>
 *
 * @author kinder
 * @since 2024-10-26
 */
public interface IQuestionService extends IService<Question> {
    /**
     * 分页查询题目
     *
     * @param questionQueryRequest 查询条件
     * @return Page<Question> 题目分页列表
     */
    public Page<Question> listQuestionByPage(@Param("questionQueryRequest") QuestionQueryRequest questionQueryRequest);

    void validQuestion(@Param("question") Question question, @Param("add") boolean add);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    void batchDeleteQuestions(List<Long> questionIdList);
}
