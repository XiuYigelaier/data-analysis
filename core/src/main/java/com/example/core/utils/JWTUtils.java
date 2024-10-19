package com.example.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
public class JWTUtils {

    // 有效期
    public static final Long JWT_TTL = 3600000L;

    // JWT令牌信息
    public static final String JWT_KEY = "HFY";

    public static String createJWT(String id, String subject, Long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }

        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setId(id)                      // 设置唯一编号
                .setSubject(subject)            // 设置主题 可以是JSON数据
                .setIssuer("admin")
                .setIssuedAt(now)               // 设置签发日期
                .setExpiration(expDate)         // 设置过期时间
                // 设置签名 使用HS256算法 并设置SecretKey(字符串)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        return builder.compact();


    }

    /**
     * 生成加密secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JWT_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析令牌数据
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
