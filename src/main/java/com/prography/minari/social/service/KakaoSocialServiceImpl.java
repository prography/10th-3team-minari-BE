package com.prography.minari.social.service;

import com.prography.minari.social.dto.social.KakaoTokenInfoResDto;
import com.prography.minari.social.dto.social.KakaoTokenResDto;
import com.prography.minari.social.dto.social.KakaoUserInfoResDto;
import com.prography.minari.user.UserRepository;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static com.prography.minari.social.dto.enums.SocialType.KAKAO;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoSocialServiceImpl implements SocialService {

    @Value("${oauth.kakao.REST_API_KEY}")
    private String CLIENT_ID;

    @Value("${oauth.kakao.REDIRECT_URI}")
    private String REDIRECT_URI;

    @Value("${oauth.kakao.REST_SECRET_KEY}")
    private String CLIENT_SECRET_KEY;

    private final UserRepository userRepository;

    @Override
    public UserLoginResDto login(String code) {

        // 토큰 받기
        KakaoTokenResDto kakaoTokenResDto =  requestToken(code);
        // 토큰 정보 보기
        KakaoTokenInfoResDto kakaoTokenInfoResDto = getTokenInfo(kakaoTokenResDto.access_token());
        // 사용자 정보 가져오기
        KakaoUserInfoResDto kakaoUserInfoResDto = requestKakaoUserInfo(kakaoTokenInfoResDto.id(), kakaoTokenResDto.access_token());
        String name = Optional.ofNullable(kakaoUserInfoResDto.kakao_account().profile().nickname())
                .orElse("미나리🌿");
        String image = Optional.ofNullable(kakaoUserInfoResDto.kakao_account().profile().profile_image_url())
                .orElse("https://picsum.photos/640/640");
        Long socialId = kakaoUserInfoResDto.id();

        // 사용자 정보로 기존 회원 조회, 없으면 새 User 객체 생성
        User user = userRepository.findBySocialTypeAndSocialId(KAKAO, socialId)
                .orElseGet(() -> userRepository.save(User.create("", KAKAO, socialId, image, name)));

        return UserLoginResDto.from(user);
    }

    // 토큰 받기 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
    private KakaoTokenResDto requestToken(String code) {
        return WebClient.builder()
                .baseUrl("https://kauth.kakao.com").build()
                .post()
                .uri("/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", CLIENT_ID)
                        .with("redirect_uri", REDIRECT_URI)
                        .with("code", code)
                        .with("client_secret", CLIENT_SECRET_KEY))
                .retrieve()
                .bodyToMono(KakaoTokenResDto.class)
                .block();
    }

    // 토큰 정보 보기 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info
    private KakaoTokenInfoResDto getTokenInfo(String accessToken) {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build()
                .get()
                .uri("/v1/user/access_token_info")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoTokenInfoResDto.class)
                .block();
    }

    // 사용자 정보 가져오기 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    private KakaoUserInfoResDto requestKakaoUserInfo(Long userId, String accessToken) {
        KakaoUserInfoResDto kakaoUserInfoResDto = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .queryParam("target_id_type", "user_id")
                        .queryParam("target_id", userId)
                        .build())
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromFormData("property_keys", "[\"kakao_account.profile\"]"))
                .retrieve()
                .bodyToMono(KakaoUserInfoResDto.class)
                .block();
        return kakaoUserInfoResDto;
    }

}
