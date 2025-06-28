package com.prography.minari.admin.controller;

import com.prography.minari.admin.controller.dto.RewardForceRequest;
import com.prography.minari.admin.service.AdminRewardService;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin/api/v1")
@RequiredArgsConstructor
public class AdminController {
    private final AdminRewardService adminRewardService;
    private final UserService userService;

    @PostMapping("/payment/force")
    public ResponseEntity forceCharge(@RequestBody RewardForceRequest request) {
        adminRewardService.giveSeedForce(request.getUserUUID(),
                request.getSeeds(),
                request.getRole(),
                request.getReason(),
                request.getMemo());
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @DeleteMapping("/users/me")
    public ResponseEntity deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteAdmin(user);
        return ResponseEntity.ok(CommonResponse.success("[ADMIN] 계정 즉시 삭제"));
    }

}
