package com.prography.minari.social.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.common.util.UuidUtil;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.social.service.impl.SocialClient;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

import static com.prography.minari.common.execption.ErrorCode.ACCOUNT_SOFT_DELETED;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final Map<String, SocialClient> socialClientMap;
    private final UserRepository userRepository;
    private final RedisProcessor redisProcessor;
    private final UuidUtil uuidUtil;
    private final JwtUtil jwtUtil;

    public UserLoginResDto login(SocialType socialType, String code, String redirectUri, HttpServletResponse response) {

        // 소셜 로그인 구현체 주입
        SocialClient socialClient = socialClientMap.get(socialType.getValue());

        // 토큰 받기
        String accessToken = socialClient.readAccessToken(code, redirectUri);

        // 사용자 정보 가져오기
        UserInfoDto userInfoDto = socialClient.readUserInfo(accessToken);

        // uuid 생성
        String uuid = uuidUtil.generateUniqueUuid();

        // 사용자 정보로 기존 회원 조회, 없으면 새 User 객체 생성
        User user = userRepository.findBySocialTypeAndSocialId(socialType, userInfoDto.socialId())
                .orElseGet(() -> userRepository.save(User.create("", socialType, userInfoDto.socialId(), userInfoDto.nickname(), userInfoDto.image(), uuid)));

        // jwt 생성
        String serverAccessToken = jwtUtil.createAccessToken(user.getId().toString());
        String serverRefreshToken = jwtUtil.createRefreshToken(user.getId().toString());

        ResponseCookie accessTokenCookie = jwtUtil.createAccessTokenCookie(serverAccessToken);
        ResponseCookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(serverRefreshToken);

        // refresh token, redis 적재
        redisProcessor.setValue(user.getId().toString(), serverRefreshToken);

        response.addHeader(SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(SET_COOKIE, refreshTokenCookie.toString());

        return UserLoginResDto.from(user, serverAccessToken, serverRefreshToken);
    }

}
