package com.prography.minari.answer.dto.res;

import java.time.LocalDate;

public record AnswerResDto(
        Long answerId,
        LocalDate answerDate,
        Long questionId
) {

    public static AnswerResDto create(Long answerId, LocalDate answerDate, Long questionId) {
        return new AnswerResDto(answerId, answerDate, questionId);
    }

}
