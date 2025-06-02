package com.prography.minari.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyUserQuestionResDto {
    private Long questionId;
    private String contents;
}
