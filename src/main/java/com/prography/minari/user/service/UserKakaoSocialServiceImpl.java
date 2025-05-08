package com.prography.minari.user.service;

import com.prography.minari.user.dto.social.KakaoTokenResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class UserKakaoSocialServiceImpl implements UserSocialService{

    @Value("${kakao.REST_API_KEY}")
    public String CLIENT_ID;

    @Value("${kakao.REDIRECT_URI}")
    public String REDIRECT_URI;

    @Value("${kakao.REST_SECRET_KEY}")
    public String CLIENT_SECRET_KEY;

    @Override
    public KakaoTokenResDto createAccessToken(String code) {
        return WebClient.builder().build()
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

}
