package com.prography.minari.social.service;

import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.social.service.impl.SocialClient;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final Map<String, SocialClient> socialClientMap;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisProcessor redisProcessor;

    public UserLoginResDto login(SocialType socialType, String code, String redirectUri) {

        // 소셜 로그인 구현체 주입
        SocialClient socialClient = socialClientMap.get(socialType.getValue());

        // 토큰 받기
        String accessToken = socialClient.readAccessToken(code, redirectUri);

        // 사용자 정보 가져오기
        UserInfoDto userInfoDto = socialClient.readUserInfo(accessToken);

        // 사용자 정보로 기존 회원 조회, 없으면 새 User 객체 생성
        User user = userRepository.findBySocialTypeAndSocialId(socialType, userInfoDto.socialId())
                .orElseGet(() -> userRepository.save(User.create("", socialType, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image())));

        // jwt 생성
        String serverAccessToken = jwtUtil.createAccessToken(user.getId());
        String serverRefreshToken = jwtUtil.createRefreshToken(user.getId());

        // refresh token, redis 적재
        Duration duration = jwtUtil.getDuration(serverRefreshToken);
        redisProcessor.setValue(user.getId().toString(), serverRefreshToken, duration);

        return UserLoginResDto.from(user, serverAccessToken, serverRefreshToken);
    }

}
