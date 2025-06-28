package com.prography.minari.user.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.dto.*;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.service.impl.SeedReader;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import com.prography.minari.user.service.impl.UserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final JwtUtil jwtUtil;
    private final RedisProcessor redisProcessor;
    private final SeedReader seedReader;

    public UserFindResDto findById(Long id) {
        User user = userReader.read(id);
        Seed seed = seedReader.readByUserId(id).orElse(new Seed(0L,user));
        return UserFindResDto.from(user, seed);
    }

    public UserJoinResDto join(UserJoinReqDto userJoinReqDto, Long userId) {
        // user 조회
        User findUser = userReader.read(userId);

        // 이미 회원가입이 완료된 사용자인 경우, 중복 가입 방지를 위해 예외 처리
        if(findUser.isRegistered())
            throw new ApiException(ErrorCode.ALREADY_REGISTERED);

        // 회원가입
        User joinUser = userWriter.join(
                findUser,
                userJoinReqDto.email(),
                userJoinReqDto.isSubscribed(),
                userJoinReqDto.emailSendTime(),
                userJoinReqDto.studyExperienceLevel(),
                userJoinReqDto.workExperienceLevel(),
                userJoinReqDto.domain()
        );

        return UserJoinResDto.from(joinUser);
    }

    public void delete(User user) {
        userWriter.delete(user);
    }

    public void deleteAdmin(User user) {
        userWriter.deleteAdmin(user);
    }

    // JWT 재발급
    public UserRefreshTokenResDto refreshToken(UserRefreshTokenReqDto userRefreshTokenReqDto) {

        String userId = jwtUtil.getUserId(userRefreshTokenReqDto.refreshToken());

        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // refreshToken 검증
        redisProcessor.validateRefreshToken(userId, userRefreshTokenReqDto.refreshToken());

        // refresh token 갱신
        Duration expiration = jwtUtil.getDuration(newRefreshToken);
        redisProcessor.setValue(userId, newRefreshToken, expiration);

        return UserRefreshTokenResDto.from(newAccessToken, newRefreshToken);
    }

    public void logout(User user) {
        redisProcessor.deleteValue(user.getId().toString());
    }
}
