package com.example.dataanalysiscalculateservice.config;

import com.example.core.entity.LoginUser;
import com.example.core.utils.JWTUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        String Token = (String) securityContext.getAuthentication().getDetails();
        template.header("Authorization",Token);
//        String userId = loginUser.getUser().getId().toString();
//        String jwt = JWTUtils.createJWT(UUID.randomUUID().toString(), userId, null);

//        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
//
//        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
//
//            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
//        } else {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (Objects.nonNull(attributes)) {
//                HttpServletRequest request = attributes.getRequest();
//                template.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
//            }
//        }
    }

}