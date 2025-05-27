package com.prography.minari.answer.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAnswerStatusResponse {
    private SttStatus status;
}
