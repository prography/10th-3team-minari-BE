package com.prography.minari.dev.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dev")
@RequiredArgsConstructor
public class DevController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/token/expired")
    public ResponseEntity<CommonResponse>  createExpiredToken(@RequestParam("userId") String userId) {
        String expiredToken = jwtUtil.createExpiredToken(userId);
        return ResponseEntity.ok(CommonResponse.success(expiredToken));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<CommonResponse> deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteAdmin(userId);
        return ResponseEntity.ok(CommonResponse.success("[ADMIN] 계정 즉시 삭제"));
    }

}
