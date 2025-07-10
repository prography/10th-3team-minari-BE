package com.prography.minari.payment.controller.docs;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import com.prography.minari.user.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "씨앗 API", description = "씨앗 관련 API입니다.")
public interface CreditApiDocs {
    @Operation(
            summary = "남은 씨앗의 개수 조회",
            description = "내가 보유중인 전체 씨앗의 수를 조회합니다. 사용 및 환불 제외"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/v1/users/credits/left")
    CommonResponse<Long> getLeftCredits(@Parameter(hidden = true) @AuthenticationPrincipal User user);
}
