package com.prography.minari.user.service;

import com.prography.minari.answer.service.impl.AnswerReader;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.service.impl.SeedReader;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.dto.UserRefreshTokenResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import com.prography.minari.user.service.impl.UserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final AnswerReader answerReader;
    private final RedisProcessor redisProcessor;
    private final SeedReader seedReader;
    private final JwtUtil jwtUtil;

    public UserFindResDto findById(Long id) {
        User user = userReader.read(id);
        Seed seed = seedReader.readByUserId(id).orElse(new Seed(0L,user));
        Long dayCount = answerReader.countDistinctAnswerDateByUserId(id);
        return UserFindResDto.from(user, seed, dayCount);
    }

    public UserJoinResDto join(UserJoinReqDto userJoinReqDto, Long userId) {
        log.info("service join");
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

    public UserRefreshTokenResDto createToken(String refreshToken) {

        // user id 추출
        String userId = jwtUtil.getUserId(refreshToken);

        // refresh token 검증
        redisProcessor.validateRefreshToken(userId, refreshToken);

        // JWT 발급
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // redis에 적재
        redisProcessor.setValue(userId, newRefreshToken);

        return UserRefreshTokenResDto.from(newAccessToken, newRefreshToken);
    }

    public void delete(User user) {
        userWriter.delete(user);
    }

    public void deleteAdmin(Long userId) {
        userWriter.deleteAdmin(userId);
    }

    public void logout(User user) {
        redisProcessor.deleteValue(user.getId().toString());
    }

    public void activate(User user) {
        userWriter.activate(user);
    }

}
