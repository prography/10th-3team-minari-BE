package com.prography.minari.admin.controller;

import com.prography.minari.admin.controller.dto.RewardForceRequest;
import com.prography.minari.admin.service.AdminRewardService;
import com.prography.minari.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin/api")
@RequiredArgsConstructor
public class AdminPaymentController {
    private final AdminRewardService adminRewardService;

    @PostMapping("/v1/payment/force")
    public CommonResponse<String> forceCharge(@RequestBody RewardForceRequest request) {
        adminRewardService.giveSeedForce(request.getUserUUID(),
                request.getSeeds(),
                request.getRole(),
                request.getReason(),
                request.getMemo());
        return CommonResponse.ok();
    }
}
