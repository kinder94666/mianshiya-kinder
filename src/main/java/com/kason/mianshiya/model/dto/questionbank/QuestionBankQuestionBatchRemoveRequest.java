package com.kason.mianshiya.model.dto.questionbank;

import lombok.Data;

import java.util.List;

@Data
public class QuestionBankQuestionBatchRemoveRequest {

    List<Long> questionIdList;

    Long questionBankId;
}
