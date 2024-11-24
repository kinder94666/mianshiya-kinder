package com.kason.mianshiya.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean sentinelFilterRegistration(RedissonConfig redissonConfig){
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CommonsRequestLoggingFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        registrationBean.setName("commonFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
