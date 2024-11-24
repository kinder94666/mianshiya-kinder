package com.kason.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring")
public class LimitProperties {
    private String sentinelDashboard;
    private List<LimitRule> limitRules;

    public List<LimitRule> getLimitRules() {
        return limitRules;
    }

    public void setLimitRules(List<LimitRule> limitRules) {
        this.limitRules = limitRules;
    }

    public String getSentinelDashboard() {
        return sentinelDashboard;
    }

    public void setSentinelDashboard(String sentinelDashboard) {
        this.sentinelDashboard = sentinelDashboard;
    }

    public static class LimitRule {
        private String resource;
        private int grade;
        private int count;

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
