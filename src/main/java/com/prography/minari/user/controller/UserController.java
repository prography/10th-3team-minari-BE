package com.prography.minari.user.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.MailService;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.controller.docs.UserApiDocs;
import com.prography.minari.user.dto.*;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController implements UserApiDocs {

    private final UserService userService;
    private final SocialService socialService;
    private final MailService mailService;
    private final RedisProcessor redisProcessor;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@PathVariable("social") String socialType,
                                @RequestParam("code") String code,
                                @RequestParam("redirect-uri") String redirectUri) {
        UserLoginResDto userLoginResDto = socialService.login(SocialType.from(socialType), code, redirectUri);

        // jwt 생성
        String serverAccessToken = jwtUtil.createAccessToken(userLoginResDto.id().toString());
        String serverRefreshToken = jwtUtil.createRefreshToken(userLoginResDto.id().toString());

        ResponseCookie accessTokenCookie = jwtUtil.createAccessTokenCookie(serverAccessToken);
        ResponseCookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(serverRefreshToken);

        // refresh token, redis 적재
        redisProcessor.setValue(userLoginResDto.id().toString(), serverRefreshToken);

        return ResponseEntity.ok()
            .header(SET_COOKIE, accessTokenCookie.toString())
            .header(SET_COOKIE, refreshTokenCookie.toString())
            .body(CommonResponse.success(userLoginResDto));
    }

    @PostMapping("/users/mail-verification")
    public ResponseEntity emailVerification(@RequestBody @Validated MailVerificationReqDto mailVerificationReqDto, @AuthenticationPrincipal User user) {
        mailService.sendAuthMail(mailVerificationReqDto, user);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/users/mail-verification/verify")
    public ResponseEntity verifyMailCode(@RequestBody MailVerificationCheckReqDto mailVerificationCheckReqDto, @AuthenticationPrincipal User user) {
        mailService.verifyAuthCode(mailVerificationCheckReqDto, user);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/users/join")
    public ResponseEntity join(@RequestBody @Validated UserJoinReqDto userJoinReqDto, @AuthenticationPrincipal User user) {
        UserJoinResDto dto = userService.join(userJoinReqDto, user.getId());
        return ResponseEntity.ok(CommonResponse.success(dto));
    }

    @GetMapping("/users/me")
    public ResponseEntity findByUserId(@AuthenticationPrincipal User user) {
        UserFindResDto dto = userService.findById(user.getId());
        return ResponseEntity.ok(CommonResponse.success(dto));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity deleteByUserId(@AuthenticationPrincipal User user) {
        userService.delete(user);
        return ResponseEntity.ok(CommonResponse.success("계정삭제"));
    }

    @PostMapping("/users/logout")
    public ResponseEntity logout(@AuthenticationPrincipal User user) {
        userService.logout(user);

        // JWT 쿠키 삭제 (Set-Cookie with Max-Age=0)
        ResponseCookie accessTokenExpiredCookie = jwtUtil.deleteAccessTokenCookie();
        ResponseCookie refreshTokenExpiredCookie = jwtUtil.deleteRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(SET_COOKIE, accessTokenExpiredCookie.toString())
                .header(SET_COOKIE, refreshTokenExpiredCookie.toString())
                .body(CommonResponse.success("로그아웃되었습니다."));
    }

}
