package com.prography.minari.user.controller.docs;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.dto.UserRefreshTokenReqDto;
import com.prography.minari.user.dto.UserRefreshTokenResDto;
import com.prography.minari.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

public interface UserApiDocs {

    @Operation(
            summary = "소셜 로그인",
            description = "소셜 타입과 인증 정보를 받아 소셜 로그인을 수행하고, JWT 토큰 및 사용자 정보를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공",
                    content = @Content(schema = @Schema(implementation = UserLoginResDto.class)))
    })
    ResponseEntity<CommonResponse<UserLoginResDto>> oauth(
            @Parameter(description = "소셜 로그인 타입 (예: kakao)", required = true) @PathVariable("social") String socialType,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "소셜 로그인 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = com.prography.minari.user.dto.UserLoginReqDto.class))
            )
            @RequestParam String code,
            @RequestParam String redirectUri
    );

    @Operation(
            summary = "이메일 인증 요청",
            description = "입력된 이메일 주소로 인증 메일을 전송합니다. (로그인된 사용자만 가능)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 요청 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Void>> emailVerification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 인증 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MailVerificationReqDto.class))
            )
            @RequestBody MailVerificationReqDto mailVerificationReqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    );

    @Operation(
            summary = "회원가입",
            description = "사용자 회원가입 정보를 받아 사용자 계정을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = UserJoinResDto.class)))
    })
    ResponseEntity<CommonResponse<UserJoinResDto>> join(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserJoinReqDto.class))
            )
            @RequestBody UserJoinReqDto userJoinReqDto, @Parameter(hidden = true) @AuthenticationPrincipal User user
    );

    @Operation(
            summary = "사용자 조회",
            description = "ID에 해당하는 사용자 정보를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserFindResDto.class)))
    })
    ResponseEntity<CommonResponse<UserFindResDto>> findByUserId(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    );

    @Operation(
            summary = "이메일 인증번호 검증",
            description = "이메일과 인증번호(코드)를 받아 인증을 검증합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "인증 실패 또는 만료",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Void>> verifyMailCode(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 인증번호 검증 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = com.prography.minari.user.dto.MailVerificationCheckReqDto.class))
            )
            @RequestBody com.prography.minari.user.dto.MailVerificationCheckReqDto req,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    );

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh Token을 받아 새로운 Access Token과 Refresh Token을 재발급합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", 
                    content = @Content(schema = @Schema(implementation = UserRefreshTokenResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 Refresh Token")
    })
    @PostMapping("/users/token/refresh")
    ResponseEntity<CommonResponse<UserRefreshTokenResDto>> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "토큰 재발급 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRefreshTokenReqDto.class))
            )
            @RequestBody UserRefreshTokenReqDto userRefreshTokenReqDto
    );

    @Operation(
            summary = "로그아웃",
            description = "현재 사용자를 로그아웃 처리합니다. Refresh Token을 Redis에서 삭제합니다. (로그인된 사용자만 가능)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/users/logout")
    ResponseEntity<CommonResponse<String>> logout(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    );
}
