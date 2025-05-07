package com.prography.minari.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/users/oauth/{social}")
    public ResponseEntity oauth(@RequestParam("code")String code) {

        System.out.println(code);

        return ResponseEntity.ok(code);
    }

}
