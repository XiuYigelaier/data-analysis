package com.analysis.dataanalysisgateway.filter;

import com.alibaba.fastjson.JSON;
import com.example.core.pojo.base.LoginUser;
import com.example.core.pojo.base.ResponseModel;
import com.example.core.utils.JWTUtils;
import com.example.core.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Component
public class JWTAuthenticationTokenFilter  implements GlobalFilter, Ordered {


     private RedisUtil redisUtil;



    private static List<String> whitelist = null;

    static {
        //加载白名单
        try (
                InputStream resourceAsStream = JWTAuthenticationTokenFilter.class.getResourceAsStream("/security-whitelist.properties");
        ) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Set<String> strings = properties.stringPropertyNames();
            whitelist= new ArrayList<>(strings);

        } catch (Exception e) {
          //  log.error("加载/security-whitelist.properties出错:{}",e.getMessage());
            e.printStackTrace();
        }


    }

    public JWTAuthenticationTokenFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //请求的url
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //白名单放行
        for (String url : whitelist) {
            if (pathMatcher.match(url, requestUrl)) {
                return chain.filter(exchange);
            }
        }
        String userId;
        //检查token是否存在
        String token = getToken(exchange);
        if (!StringUtils.hasText(token)){
            return buildReturnMono("没有认证",exchange);
        }
        try {
            Claims claim = JWTUtils.parseJWT(token);
            userId = claim.getSubject();
        } catch (Exception e) {
            return buildReturnMono("jwt令牌异常",exchange);
        }
        String redisKey = "login:"+userId;
        LoginUser user = (LoginUser) redisUtil.get(redisKey);
        if(ObjectUtils.isEmpty(user)){
            return buildReturnMono("redis无对应数据",exchange);

        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContext ctx = SecurityContextHolder.getContext();

        return chain.filter(exchange);
        //判断是否是有效的token

    }

    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (!StringUtils.hasText(token)) {
            return null;
        }
        return token;
    }

    private Mono<Void> buildReturnMono(String error, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        String jsonString = JSON.toJSONString( ResponseModel.failure("验证失败:"+error));
        byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        String requestURI = request.getRequestURI();
//
//        if (whitelist.contains(requestURI)) {
//            filterChain.doFilter(request,response);
//        }
//            // 请求在白名单中，放行
//            filterChain.doFilter(request, response);
//            String authHeader = request.getHeader("Authorization");
//        if (!StringUtils.hasText(authHeader) ) {
//            filterChain.doFilter(request,response);
//            throw new RuntimeException("未认证");
//        }
//        String token = authHeader.substring(7);
//        String userId;
//        try {
//            Claims claim = JWTUtils.parseJWT(token);
//            userId = claim.getSubject();
//        } catch (Exception e) {
//            throw new RuntimeException("令牌异常", e);
//        }
//        String redisKey = "login:" + userId;
//        LoginUser loginUser = (LoginUser) redisUtil.get(redisKey);
//        if(ObjectUtils.isEmpty(loginUser)){
//            throw  new RuntimeException("redis无数据:"+redisKey);
//
//        }
//        //todo 权限信息
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//        filterChain.doFilter(request,response);
//
//    }
}
