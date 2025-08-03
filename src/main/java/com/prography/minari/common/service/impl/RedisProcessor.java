package com.prography.minari.common.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.prography.minari.common.execption.ErrorCode.ENTITY_NOT_FOUND;
import static com.prography.minari.common.execption.ErrorCode.JWT_NOT_MATCHED;

@Slf4j
@ImplService
@RequiredArgsConstructor
public class RedisProcessor {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    /**
     * Redis에 데이터 저장 (만료시간 설정)
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value, jwtUtil.getDuration(value));
        log.info("refresh token 저장 : {}", value);
    }

    public void setValue(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
        log.info("[REDIS] {} : {}", key, value);
    }

    /**
     * Redis에서 데이터 조회
     */
    public Optional<Object> getValue(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    /**
     * Redis에서 데이터 삭제
     */
    public Boolean deleteValue(String key) {
        log.info("refresh token 삭제 : {}", key);
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