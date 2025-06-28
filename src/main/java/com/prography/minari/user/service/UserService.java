package com.prography.minari.user.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.dto.UserRefreshTokenResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import com.prography.minari.user.service.impl.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final JwtUtil jwtUtil;

    public UserFindResDto findById(Long id) {
        User user = userReader.read(id);
        return UserFindResDto.from(user);
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

    public UserRefreshTokenResDto refreshToken(Long userId) {
        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);
        return UserRefreshTokenResDto.from(accessToken, refreshToken);
    }
}
