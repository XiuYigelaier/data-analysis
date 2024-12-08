package com.example.core.filter;

import com.example.core.entity.LoginUser;
import com.example.core.utils.JWTUtils;
import com.example.core.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter implements Filter {

   private  RedisUtil redisUtil;

    public TokenFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // 从请求头中获取Token
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (!StringUtils.hasText(token)) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String userId;
                try {
                    Claims claim = JWTUtils.parseJWT(token);
                    userId = claim.getSubject();
                } catch (Exception e) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String redisKey = "login:" + userId;
                LoginUser user = (LoginUser) redisUtil.get(redisKey);
                if (ObjectUtils.isEmpty(user)) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;

                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
                usernamePasswordAuthenticationToken.setDetails(authHeader);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContext ctx = SecurityContextHolder.getContext();
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
