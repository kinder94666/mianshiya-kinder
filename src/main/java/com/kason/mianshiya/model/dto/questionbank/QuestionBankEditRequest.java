package com.kason.mianshiya.model.dto.questionbank;

import lombok.Data;

@Data
public class QuestionBankEditRequest {
    /**
     * id
     */
    private Long id;

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

    private static final long serialVersionUID = 1L;


}
