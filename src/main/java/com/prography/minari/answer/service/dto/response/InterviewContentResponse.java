package com.prography.minari.answer.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@Schema(description = "인터뷰 응답 DTO")
public class InterviewContentResponse {
    @Schema(description = "질문 정답")
    private String answer;

    @Schema(description = "질문")
    private String question;

    @Schema(description = "사용자 답변")
    private String reply;

    @Schema(description = "생성 일자")
    private LocalDate createDate;

    @Schema(description = "답변 러닝 타임 (초)")
    private Double runningTime;
}
