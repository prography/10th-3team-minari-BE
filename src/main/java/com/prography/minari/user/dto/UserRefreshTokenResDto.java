package com.prography.minari.user.dto;

public record UserRefreshTokenResDto(
        String accessToken,
        String refreshToken
) {

    public static UserRefreshTokenResDto from(String accessToken, String refreshToken) {
        return new UserRefreshTokenResDto(accessToken, refreshToken);
    }
}
