package com.prography.minari.question.controller.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertSttMemoRequest {
    @Schema(description = "메모 내용", example = "오늘은 이런 생각을 했어요.")
    private String memo;
}
