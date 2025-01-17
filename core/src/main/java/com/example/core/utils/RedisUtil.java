package com.example.core.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {


     private RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate = redisTemplate;
    }

    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            this.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public void remove(String key) {
        if (this.exists(key)) {
            this.redisTemplate.delete(key);
        }

    }
    public boolean existsInZSet(String zsetName, String key) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        Double score = zSetOps.score(zsetName, key);
        return score != null;
    }

    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public Object get(String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public void add(String key, Object value) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        set.add(key, new Object[]{value});
    }

    public void addZSet(String key, Object val, Double rank){
        redisTemplate.opsForZSet().add(key,val,rank);
    }
    public void removeZSet(String key) {
        removeZSetRange(key, 0L, getZSetSize(key));
    }

    public void removeZSetRange(String key, Long start, Long end) {
        redisTemplate.boundZSetOps(key).removeRange(start, end);
    }

    public long getZSetSize(String key) {
        return redisTemplate.boundZSetOps(key).size();
    }


    public <T> List<T> getZSet(String key){
        Set<ZSetOperations.TypedTuple<T>> sortedSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, Double.MAX_VALUE);
        List<T> ans = new ArrayList<>();

        if (sortedSet == null || sortedSet.isEmpty()) {
            System.out.println("No data found for key: " + key);
            return ans;
        }

        for (ZSetOperations.TypedTuple<T> tuple : sortedSet) {
            ans.add(tuple.getValue());
        }

        return ans;
    }







}
