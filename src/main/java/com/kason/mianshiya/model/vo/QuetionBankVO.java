package com.kason.mianshiya.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kason.mianshiya.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
@Data
public class QuetionBankVO implements Serializable {

    Page<Question> questionPage;
}
