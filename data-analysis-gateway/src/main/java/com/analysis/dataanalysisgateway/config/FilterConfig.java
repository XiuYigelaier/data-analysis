//package com.analysis.dataanalysisgateway.config;
//
//import com.analysis.dataanalysisgateway.filter.JWTAuthenticationTokenFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig  {
//    @Autowired
//    JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//
//    @Bean
//    public FilterRegistrationBean<JWTAuthenticationTokenFilter> loggingFilter(){
//        FilterRegistrationBean<JWTAuthenticationTokenFilter> registrationBean
//                = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(jwtAuthenticationTokenFilter);
//        registrationBean.addUrlPatterns("/*"); // 设置过滤器的URL模式
//
//        return registrationBean;
//    }
//}
