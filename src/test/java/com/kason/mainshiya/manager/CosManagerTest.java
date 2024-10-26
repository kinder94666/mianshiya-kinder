package com.kason.mianshiya.manager;

import com.kason.mianshiya.MainApplication;
import com.kason.mianshiya.manager.CosManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Cos 操作测试
 *
 * @author <a href="https://github.com/likason">kason</a>
 * @from <a href="https://kason.icu">编程导航知识星球</a>
 */
@SpringBootTest(classes = MainApplication.class)
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void putObject() {
        cosManager.putObject("test", "test.json");
    }
}