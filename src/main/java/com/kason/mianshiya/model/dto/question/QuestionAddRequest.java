package com.kason.mianshiya.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    private String title;


    private String content;


    private List<String> tags;


    private String answer;


}
