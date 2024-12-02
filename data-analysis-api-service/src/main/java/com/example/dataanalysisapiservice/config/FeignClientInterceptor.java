package com.example.dataanalysisapiservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
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
