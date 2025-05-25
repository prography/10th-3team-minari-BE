package com.prography.minari.social.service;

import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.social.service.impl.SocialReader;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final Map<String, SocialReader> socialReaderMap;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserLoginResDto login(SocialType socialType, String code, String redirectUri) {

        // 소셜 로그인 구현체 주입
        SocialReader socialReader = socialReaderMap.get(socialType.getValue());

        // 토큰 받기
        String accessToken = socialReader.readAccessToken(code, redirectUri);

        // 사용자 정보 가져오기
        UserInfoDto userInfoDto = socialReader.readUserInfo(accessToken);

        // 사용자 정보로 기존 회원 조회, 없으면 새 User 객체 생성
        User user = userRepository.findBySocialTypeAndSocialId(socialType, userInfoDto.socialId())
                .orElseGet(() -> userRepository.save(User.create("", socialType, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image())));

        // jwt 생성 TODO Spring Security 도입시, 차후에 제거될 예정
        String jwt = jwtUtil.createToken(user.getId().toString());

        return UserLoginResDto.from(user, jwt);
    }

}
