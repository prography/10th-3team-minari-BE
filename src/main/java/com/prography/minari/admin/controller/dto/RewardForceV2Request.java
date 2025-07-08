package com.prography.minari.admin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RewardForceV2Request {
    @Schema(description = "유저 UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userUUID;

    @Schema(description = "지급할 씨앗 수, 씨앗을 개수로 주길 원할때만 사용됩니다. 상품을 기준으로 할때는 null 보내주세요", example = "100")
    private Integer seeds;

    @Schema(description = "역할 (예: 사용자, 관리자)", example = "USER")
    private String role;

    @Schema(description = "지급 사유", example = "이벤트 참여 보상")
    private String reason;

    @Schema(description = "메모 (선택사항)", example = "5월 출석 이벤트")
    private String memo;

    @Schema(description = "관련 상품 ID (있을 경우). 상품이 아닌 개수로 주길 원한다면 null을 보내주세요", example = "12345")
    private Long productId;
}
