package com.prography.minari.admin.controller;

import com.prography.minari.admin.controller.docs.AdminApiDocs;
import com.prography.minari.admin.controller.dto.RewardForceRequest;
import com.prography.minari.admin.controller.dto.RewardForceV2Request;
import com.prography.minari.admin.service.AdminRewardService;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApiDocs {
    private final AdminRewardService adminRewardService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/payment/force")
    public ResponseEntity forceCharge(@RequestBody RewardForceRequest request) {
        adminRewardService.giveSeedForce(request.getUserUUID(),
                request.getSeeds(),
                request.getRole(),
                request.getReason(),
                request.getMemo());
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/payment/force/v2")
    public ResponseEntity<CommonResponse<String>> forceChargeV2(@RequestBody RewardForceV2Request request) {
        adminRewardService.giveSeedForceV2(request.getUserUUID(),
                request.getSeeds(),
                request.getRole(),
                request.getReason(),
                request.getMemo(),
                request.getProductId());
        return ResponseEntity.ok(CommonResponse.ok());
    }

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
