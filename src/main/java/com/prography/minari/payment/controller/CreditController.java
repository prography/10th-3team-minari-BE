package com.prography.minari.payment.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.payment.service.CreditService;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;

    @GetMapping("/users/credits/left")
    public CommonResponse<Long> getSellingProducts(@AuthenticationPrincipal User user) {
        Long left= creditService.leftCredits(user.getId());
        return CommonResponse.success(left);
    }
}
