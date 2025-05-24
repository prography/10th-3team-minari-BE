package com.prography.minari.social.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.social.dto.social.KakaoTokenInfoResDto;
import com.prography.minari.social.dto.social.KakaoTokenResDto;
import com.prography.minari.social.dto.social.KakaoUserInfoResDto;
import com.prography.minari.social.dto.social.UserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@ImplService("kakao")
public class KakaoSocialReader implements SocialReader {

    @Value("${oauth.kakao.REST_API_KEY:default}")
    private String CLIENT_ID;

    @Value("${oauth.kakao.REDIRECT_URI:default}")
    private String REDIRECT_URI;

    @Value("${oauth.kakao.REST_SECRET_KEY:default}")
    private String CLIENT_SECRET_KEY;

    @Override
    public String readAccessToken(String code) {
            return WebClient.builder()
                    .baseUrl("https://kauth.kakao.com").build()
                    .post()
                    .uri("/oauth/token")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", CLIENT_ID)
                            .with("redirect_uri", REDIRECT_URI)
                            .with("code", code)
                            .with("client_secret", CLIENT_SECRET_KEY))
                    .retrieve()
                    .bodyToMono(KakaoTokenResDto.class)
                    .block()
                    .access_token();
    }

    @Override
    public UserInfoDto readUserInfo(String accessToken) {

        // 토큰 정보 보기
        Long userId = readTokenId(accessToken);

        // 사용자 정보 가져오기 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .queryParam("target_id_type", "user_id")
                        .queryParam("target_id", userId)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(BodyInserters.fromFormData("property_keys", "[\"kakao_account.profile\"]"))
                .retrieve()
                .bodyToMono(KakaoUserInfoResDto.class)
                .block()
                .to();
    }

    // 토큰 정보 보기 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info
    private Long readTokenId(String accessToken) {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build()
                .get()
                .uri("/v1/user/access_token_info")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoTokenInfoResDto.class)
                .block()
                .id();
    }

}
