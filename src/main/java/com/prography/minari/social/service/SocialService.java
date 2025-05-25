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

    public UserLoginResDto login(SocialType socialType, String code) {

        // 소셜 로그인 구현체 주입
        SocialReader socialReader = socialReaderMap.get(socialType.getValue());

        // 토큰 받기
        String accessToken = socialReader.readAccessToken(code);

        // 사용자 정보 가져오기
        UserInfoDto UserInfoDto = socialReader.readUserInfo(accessToken);

        // 사용자 정보 추출
        String name   = Optional.ofNullable(UserInfoDto.nickname()).orElse("미나리🌿");                     // 소셜 서비스에서 이름을 제공하지 않는 경우, 기본값 할당
        String image  = Optional.ofNullable(UserInfoDto.image()).orElse("https://picsum.photos/640/640"); // 소셜 서비스에서 이미지를 제공하지 않는 경우, 기본값 할당
        Long socialId = UserInfoDto.socialId();                                                                 // 소셜 ID

        // 사용자 정보로 기존 회원 조회, 없으면 새 User 객체 생성
        User user = userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .orElseGet(() -> userRepository.save(User.create("", socialType, socialId, name, image)));

        // jwt 생성
        String jwt = jwtUtil.createToken(user.getId().toString());

        return UserLoginResDto.from(user, jwt);
    }

}
