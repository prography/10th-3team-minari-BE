package com.prography.minari.common.service.impl;

import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@ImplService
@RequiredArgsConstructor
public class RedisProcessor {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에 데이터 저장 (만료시간 설정)
     */
    public void setValue(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
        log.info("refresh token 저장 : {}", value);
    }

    /**
     * Redis에서 데이터 조회
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Redis에서 데이터 삭제
     */
    public Boolean deleteValue(String key) {
        log.info("refresh token 삭제 : {}", getValue(key));
        return redisTemplate.delete(key);
    }

    /**
     * Redis에 키가 존재하는지 확인
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Redis에 키의 만료시간 설정
     */
    public Boolean expire(String key, Duration duration) {
        return redisTemplate.expire(key, duration);
    }

    /**
     * Redis에 키의 만료시간 조회
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
} 