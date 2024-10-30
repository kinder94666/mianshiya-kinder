package com.kason.mianshiya.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class QuestionUpdateRequest implements Serializable {
    public static final long serialVersionUID = 1L;

    private Long id;
    private String title;

    private String content;

    private List<String> tags;

    private String answer;
}
