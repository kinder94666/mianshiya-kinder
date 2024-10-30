package com.kason.mianshiya.model.dto.questionbank;

import com.kason.mianshiya.common.PageRequest;
import lombok.Data;

@Data
public class QuestionBankQueryRequest extends PageRequest implements java.io.Serializable{


    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 创建用户 id
     */
    private Long userId;
    long getQuestionBankId;

    boolean needQuestionList;

    private static final long serialVersionUID = 1L;
}
