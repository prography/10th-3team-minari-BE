package com.prography.minari.user.controller;

import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final Map<String, SocialService> socialServiceMap;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code")String code, @PathVariable("social") String social) {
        UserLoginResDto userLoginResDto = socialServiceMap.get(social).login(code);
        return ResponseEntity.ok(userLoginResDto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity findByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
