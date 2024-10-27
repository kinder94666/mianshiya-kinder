package com.kason.mianshiya.model.dto.questionbank;

import com.kason.mianshiya.common.PageRequest;
import lombok.Data;

@Data
public class QuestionBankQueryRequest extends PageRequest implements java.io.Serializable{

    long getQuestionBankId;

    boolean needQuestionList;

    private static final long serialVersionUID = 1L;
}
