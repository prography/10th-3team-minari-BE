package com.prography.minari.user.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.MailService;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.controller.docs.UserApiDocs;
import com.prography.minari.user.dto.*;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController implements UserApiDocs {

    private final UserService userService;
    private final SocialService socialService;
    private final MailService mailService;

    @PostMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@PathVariable("social") String socialType, @RequestBody @Validated UserLoginReqDto userLoginReqDto) {
        UserLoginResDto userLoginResDto = socialService.login(SocialType.from(socialType), userLoginReqDto.code(), userLoginReqDto.redirectUri());
        return ResponseEntity.ok(CommonResponse.success(userLoginResDto));
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

}
