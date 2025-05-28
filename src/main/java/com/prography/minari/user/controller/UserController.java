package com.prography.minari.user.controller;

import com.prography.minari.common.response.ApiResponse;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.MailService;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final SocialService socialService;
    private final MailService mailService;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code") String code, @RequestParam("redirect-uri") String redirectUri, @PathVariable("social") String socialType) {
        UserLoginResDto userLoginResDto = socialService.login(SocialType.from(socialType), code, redirectUri);
        return ResponseEntity.ok(userLoginResDto);
    }

    @PostMapping("/users/mail-verification")
    public ResponseEntity emailVerification(@RequestBody MailVerificationReqDto mailVerificationReqDto) {
        mailService.sendAuthMail(mailVerificationReqDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity findByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
