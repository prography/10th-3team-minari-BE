package com.prography.minari.user.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.MailService;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserJoinReqDto;
import com.prography.minari.user.dto.UserJoinResDto;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.service.UserService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final SocialService socialService;
    private final MailService mailService;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity<CommonResponse<UserLoginResDto>> oauth(@RequestParam("code") String code, @RequestParam("redirect-uri") String redirectUri, @PathVariable("social") String socialType) {
        UserLoginResDto userLoginResDto = socialService.login(SocialType.from(socialType), code, redirectUri);
        return ResponseEntity.ok(CommonResponse.success(userLoginResDto));
    }

    @PostMapping("/users/mail-verification")
    public ResponseEntity<CommonResponse<String>> emailVerification(@RequestBody @Validated MailVerificationReqDto mailVerificationReqDto) {
        mailService.sendAuthMail(mailVerificationReqDto);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/users/join")
    public ResponseEntity<CommonResponse<UserJoinResDto>> join(@RequestBody @Validated UserJoinReqDto userJoinReqDto) {
        UserJoinResDto userJoinResDto = userService.join(userJoinReqDto);
        return ResponseEntity.ok(CommonResponse.success(userJoinResDto));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<CommonResponse<UserFindResDto>> findByUserId(@PathVariable("id") Long id) {
        UserFindResDto userFindResDto = userService.findById(id);
        return ResponseEntity.ok(CommonResponse.success(userFindResDto));
    }

}
