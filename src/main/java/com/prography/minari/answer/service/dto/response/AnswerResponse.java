package com.prography.minari.answer.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponse {
    private String answer;
    private String question;
    private String reply;
}
