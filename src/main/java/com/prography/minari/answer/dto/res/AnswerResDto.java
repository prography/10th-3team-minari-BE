package com.prography.minari.answer.dto.res;

public record AnswerResDto(
        String answerId,
        String answerDate,
        String questionId
) {

    public AnswerResDto create(String answerId, String answerDate, String questionId) {
        return new AnswerResDto(answerId, answerDate, questionId);
    }

}
