package com.kason.mianshiya.model.dto.question;

import com.kason.mianshiya.common.PageRequest;
import lombok.Data;

@Data
public class QuestionQueryRequest extends PageRequest implements java.io.Serializable{

    private Long questionBankId;

}
