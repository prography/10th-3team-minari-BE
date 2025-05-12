package com.prography.minari.user.controller;

import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Map<String, SocialService> socialServiceMap;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code")String code, @PathVariable("social") String social) {

        Object result = socialServiceMap.get(social).join(code);

        return ResponseEntity.ok(result);
    }

}
