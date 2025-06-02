package com.prography.minari.user.service;

import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
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

    public UserFindResDto findById(Long id) {
        User user = userReader.read(id);
        return UserFindResDto.from(user);
    }

    public UserJoinResDto join(UserJoinReqDto userJoinReqDto) {
        // user 조회 TODO Spring Security 도입 이후 파라미터에서 UserID 제거
        User findUser = userReader.read(userJoinReqDto.userId());

        // 회원가입
        User joinUser = userWriter.join(
                findUser,
                userJoinReqDto.isSubscribed(),
                userJoinReqDto.emailSendTime(),
                userJoinReqDto.studyExperienceLevel(),
                userJoinReqDto.workExperienceLevel(),
                userJoinReqDto.domain()
        );

        return UserJoinResDto.from(joinUser);

    }

}
