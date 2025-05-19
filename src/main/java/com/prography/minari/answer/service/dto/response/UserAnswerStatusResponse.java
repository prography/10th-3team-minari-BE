package com.prography.minari.answer.service.dto.response;

import com.prography.minari.answer.service.dto.SttStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAnswerStatusResponse {
    private SttStatus status;
}
