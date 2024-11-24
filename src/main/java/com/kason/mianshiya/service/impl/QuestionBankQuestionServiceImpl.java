package com.kason.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kason.mianshiya.common.ErrorCode;
import com.kason.mianshiya.constant.CommonConstant;
import com.kason.mianshiya.exception.BusinessException;
import com.kason.mianshiya.exception.ThrowUtils;
import com.kason.mianshiya.mapper.QuestionBankQuestionMapper;
import com.kason.mianshiya.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.kason.mianshiya.model.entity.Question;
import com.kason.mianshiya.model.entity.QuestionBank;
import com.kason.mianshiya.model.entity.QuestionBankQuestion;
import com.kason.mianshiya.model.entity.User;
import com.kason.mianshiya.model.vo.QuestionBankQuestionVO;
import com.kason.mianshiya.model.vo.UserVO;
import com.kason.mianshiya.service.IQuestionBankQuestionService;
import com.kason.mianshiya.service.IQuestionBankService;
import com.kason.mianshiya.service.IQuestionService;
import com.kason.mianshiya.service.UserService;
import com.kason.mianshiya.utils.SqlUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Resource
    private UserService userService;

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

    @Override
    public void deleteByBankId(Long bankId) {

        LambdaQueryChainWrapper<QuestionBankQuestion> wrapper = this
                .lambdaQuery()
                .eq(QuestionBankQuestion::getQuestionBankId, bankId);
        this.remove(wrapper);
    }
    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        String sortField = questionBankQuestionQueryRequest.getSortField();
        String sortOrder = questionBankQuestionQueryRequest.getSortOrder();
        Long questionBankId = questionBankQuestionQueryRequest.getQuestionBankId();
        Long questionId = questionBankQuestionQueryRequest.getQuestionId();
        Long userId = questionBankQuestionQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 分页获取题库题目关联封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request) {
        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionPage.getRecords();
        Page<QuestionBankQuestionVO> questionBankQuestionVOPage = new Page<>(questionBankQuestionPage.getCurrent(), questionBankQuestionPage.getSize(), questionBankQuestionPage.getTotal());
        if (CollUtil.isEmpty(questionBankQuestionList)) {
            return questionBankQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankQuestionVO> questionBankQuestionVOList = questionBankQuestionList.stream().map(questionBankQuestion -> {
            return QuestionBankQuestionVO.objToVo(questionBankQuestion);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBankQuestionList.stream().map(QuestionBankQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionBankQuestionVOList.forEach(questionBankQuestionVO -> {
            Long userId = questionBankQuestionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankQuestionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionBankQuestionVOPage.setRecords(questionBankQuestionVOList);
        return questionBankQuestionVOPage;
    }


    /**
     * 获取题库题目关联封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    @Override
    public QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankQuestionVO questionBankQuestionVO = QuestionBankQuestionVO.objToVo(questionBankQuestion);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBankQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankQuestionVO.setUser(userVO);
        // endregion

        return questionBankQuestionVO;
    }

    /**
     * 批量添加题目到题库
     * @param questionIdList 题目id列表
     * @param questionBankId 题库列表
     * @param loginUser 用户
     */
    @Override
    public void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, User loginUser) {
        //1. 参数校验
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ErrorCode.PARAMS_ERROR, "题目id列表不能为空");
        ThrowUtils.throwIf(questionBankId == null||questionBankId<=0, ErrorCode.PARAMS_ERROR, "题库id不能为空");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        //2. 查询题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        //3. 查询题目是否存在
        List<Question> questionList = questionService.listByIds(questionIdList);
        List<Long> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIds), ErrorCode.NOT_FOUND_ERROR, "题目不存在");


        //4. 批量插入

        for (Long questionId : questionIds) {
            QuestionBankQuestion qbq = QuestionBankQuestion.builder()
                    .questionBankId(questionBankId)
                    .questionId(questionId)
                    .userId(loginUser.getId())
                    .build();

            if(!this.save(qbq)){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"插入失败");
            }
        }

    }

    @Override
    public void batchRemoveQuestionsFromBank(List<Long> questionIdList, Long questionBankId) {
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ErrorCode.PARAMS_ERROR, "题目id列表不能为空");

        ThrowUtils.throwIf(questionBankId == null||questionBankId<=0, ErrorCode.PARAMS_ERROR, "题库id不能为空");

        LambdaQueryWrapper<QuestionBankQuestion> wrapper = Wrappers.<QuestionBankQuestion>lambdaQuery()
                .in(QuestionBankQuestion::getQuestionId, questionIdList)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
        boolean remove = this.remove(wrapper);
        if(!remove){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"删除失败");
        }


    }
}
