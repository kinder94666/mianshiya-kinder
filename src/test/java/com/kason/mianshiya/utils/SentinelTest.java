package com.kason.mianshiya.utils;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.kason.mianshiya.MainApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MainApplication.class)
// 如果在根路径下没有application类则需要指定一个application类

public class SentinelTest {
    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
//    @Test
//    void testSentinal(){
//        initFlowRules();
//
//        while(true){
//            try(Entry entry = SphU.entry("HelloWorld")){
//                // Your business logic here.
//                System.out.println("hello world");
//            }catch(BlockException ex){
//                // Handle the block case.
//                System.out.println("blocked!");
//            }
//        }
//    }



}
