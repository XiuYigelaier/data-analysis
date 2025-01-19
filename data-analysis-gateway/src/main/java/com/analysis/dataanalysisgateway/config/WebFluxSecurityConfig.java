//package com.analysis.dataanalysisgateway.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//
//@EnableWebFluxSecurity
//@Configuration
//public class WebFluxSecurityConfig {
//    @Bean
//    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
//        return http
//                .cors().and()  // 启用CORS
//                .csrf().disable()
//                .authorizeExchange()
//                .pathMatchers("/**").permitAll()
//                .anyExchange().authenticated()
//                .and()
//                .build();
//    }
//
//
//}
//
//
//
//
