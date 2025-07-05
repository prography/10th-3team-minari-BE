package com.prography.minari.question.dto.res;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.question.entity.Question;

public record QuestionResDto(
        Long questionId,
        String content,
        String answer,
        String tag,
        Domain domain
) {

    public static QuestionResDto from(Question question) {
        return new QuestionResDto(
                question.getId(),
                question.getContent(),
                question.getAnswer(),
                question.getTag(),
                question.getDomain());
    }
}
