package com.prography.minari.user.controller;

import com.prography.minari.social.service.SocialService;
import com.prography.minari.user.dto.UserFindResDto;
import com.prography.minari.user.dto.UserLoginResDto;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Map<String, SocialService> socialServiceMap;

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code")String code, @PathVariable("social") String social) {

        UserLoginResDto userLoginResDto = socialServiceMap.get(social).login(code);

        // 신규 회원일 경우, 회원 등록 절차를 위한 redirect
        if(userLoginResDto.isNotRegistered()) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("/")) // 리다이렉트할 경로
                    .build();
        }

        return ResponseEntity.ok(userLoginResDto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity findByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
