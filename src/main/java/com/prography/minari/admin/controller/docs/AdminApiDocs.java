package com.prography.minari.admin.controller.docs;

import com.prography.minari.admin.controller.dto.RewardForceV2Request;
import com.prography.minari.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "어드민 API", description = "어드민 관련 API입니다.")
public interface AdminApiDocs {
    @Operation(
            summary = "씨앗 수기 지급",
            description = "계좌이체 및 오류 상황시 수기로 씨앗을 지급하는 api"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @PostMapping("/admin/api/v1/payment/force/v2")
    ResponseEntity<CommonResponse<String>> forceChargeV2(@RequestBody RewardForceV2Request request);
}
