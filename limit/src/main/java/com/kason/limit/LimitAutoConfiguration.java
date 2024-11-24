package com.kason.limit;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(LimitProperties.class)
public class LimitAutoConfiguration {

    @Resource
    private LimitProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @PostConstruct
    public void initLimit() {
        initDefaultRule();
        initDashboard();
    }


    private void initDefaultRule() {
        List<LimitProperties.LimitRule> limitRules = properties.getLimitRules();
        if (CollectionUtils.isEmpty(limitRules)) {
            return;
        }

        List<FlowRule> rules = new ArrayList<>();
        for (LimitProperties.LimitRule limitRule : limitRules) {
            FlowRule rule = new FlowRule();
            rule.setResource(limitRule.getResource());
            rule.setGrade(limitRule.getGrade());
            rule.setCount(limitRule.getCount());
            rules.add(rule);
        }
        FlowRuleManager.loadRules(rules);
    }

    private void initDashboard() {
        SentinelConfig.setConfig("csp.sentinel.dashboard.server", properties.getSentinelDashboard());
    }
}
