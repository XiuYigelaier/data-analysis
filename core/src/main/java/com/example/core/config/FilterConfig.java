package com.example.core.config;

import com.example.core.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<TokenFilter> tokenFilterRegistration() {
//        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(new TokenFilter());
//        registrationBean.addUrlPatterns("/api/*"); // 将过滤器应用于所有URL模式
//        // 您还可以设置过滤器的其他属性，比如顺序（order）
//
//        return registrationBean;
//    }
//}
