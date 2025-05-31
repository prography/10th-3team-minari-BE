package com.prography.minari.answer.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewContentResponse {
    private String answer;
    private String question;
    private String reply;

    public static InterviewContentResponse of(String answer, String question, String reply) {
        return new InterviewContentResponse(answer, question, reply);
    }
}
