package com.kason.mianshiya.sentinel;

import cn.hutool.core.io.FileUtil;
import com.alibaba.csp.sentinel.datasource.*;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.File;
import java.util.List;

public class SentinelRulesManager {

    /**
     * 持久化配置为本地文件
     */
    public void listenRules() throws Exception {
        // 获取项目根目录
        String rootPath = System.getProperty("user.dir");
        // sentinel 目录路径
        File sentinelDir = new File(rootPath, "sentinel");
        // 目录不存在则创建
        if (!FileUtil.exist(sentinelDir)) {
            FileUtil.mkdir(sentinelDir);
        }
        // 规则文件路径
        String flowRulePath = new File(sentinelDir, "FlowRule.json").getAbsolutePath();
        String degradeRulePath = new File(sentinelDir, "DegradeRule.json").getAbsolutePath();

        // Data source for FlowRule
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new FileRefreshableDataSource<>(flowRulePath, flowRuleListParser);
        // Register to flow rule manager.
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
        WritableDataSource<List<FlowRule>> flowWds = new FileWritableDataSource<>(flowRulePath, this::encodeJson);
        // Register to writable data source registry so that rules can be updated to file
        WritableDataSourceRegistry.registerFlowDataSource(flowWds);

        // Data source for DegradeRule
        FileRefreshableDataSource<List<DegradeRule>> degradeRuleDataSource
                = new FileRefreshableDataSource<>(
                degradeRulePath, degradeRuleListParser);
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
        WritableDataSource<List<DegradeRule>> degradeWds = new FileWritableDataSource<>(degradeRulePath, this::encodeJson);
        // Register to writable data source registry so that rules can be updated to file
        WritableDataSourceRegistry.registerDegradeDataSource(degradeWds);
    }

    private Converter<String, List<FlowRule>> flowRuleListParser = source -> JSON.parseObject(source,
            new TypeReference<List<FlowRule>>() {
            });
    private Converter<String, List<DegradeRule>> degradeRuleListParser = source -> JSON.parseObject(source,
            new TypeReference<List<DegradeRule>>() {
            });

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }

}
