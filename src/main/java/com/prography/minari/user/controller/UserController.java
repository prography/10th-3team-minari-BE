package com.prography.minari.user.controller;

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

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code")String code, @PathVariable("social") String socialType) {
        UserLoginResDto userLoginResDto = socialService.login(SocialType.from(socialType), code);
        return ResponseEntity.ok(userLoginResDto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity findByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
