package com.prography.minari.answer.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InterviewContentResponse {
    private String answer;
    private String question;
    private String reply;
    private LocalDate createDate;
    private Double runningTime;

}
