package com.prography.minari.user.controller.docs;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.dto.UserLoginResDto;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @PostMapping("/users/oauth/{social}")
    ResponseEntity<CommonResponse<UserLoginResDto>> oauth(
            @Parameter(description = "소셜 로그인 타입 (예: kakao)", required = true) @PathVariable("social") String socialType,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "소셜 로그인 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = com.prography.minari.user.dto.UserLoginReqDto.class))
            )
            @RequestBody com.prography.minari.user.dto.UserLoginReqDto userLoginReqDto
    );

    @Operation(
            summary = "이메일 인증 요청",
            description = "입력된 이메일 주소로 인증 메일을 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 요청 성공")
    })
    @PostMapping("/users/mail-verification")
    ResponseEntity<CommonResponse<Void>> emailVerification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 인증 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MailVerificationReqDto.class))
            )
            @RequestBody MailVerificationReqDto mailVerificationReqDto
    );

    @Operation(
            summary = "회원가입",
            description = "사용자 회원가입 정보를 받아 사용자 계정을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping("/users/join")
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공")
    })
    @GetMapping("/users/me")
    ResponseEntity<CommonResponse<UserFindResDto>> findByUserId(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    );
}
