package com.prography.minari.answer.dto.res;

import java.time.LocalDate;

public record AnswerResDto(
        boolean isExisted,
        Long answerId,
        LocalDate answerDate,
        Long questionId
) {

    private static AnswerResDto create(boolean isExisted, Long answerId, LocalDate answerDate, Long questionId) {
        return new AnswerResDto(isExisted, answerId, answerDate, questionId);
    }

    public static AnswerResDto createExisted(Long answerId, LocalDate answerDate, Long questionId) {
        return create(true, answerId, answerDate, questionId);
    }

    public static AnswerResDto createEmpty(LocalDate answerDate) {
        return create(false, null, answerDate, null);
    }

}
