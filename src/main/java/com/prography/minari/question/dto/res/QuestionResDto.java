package com.prography.minari.question.dto.res;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.question.entity.Question;

import java.util.List;

public record QuestionResDto(
        Long questionId,
        String content,
        String answer,
        List<String> tag,
        Domain domain
) {

    public static QuestionResDto from(Question question) {
        return new QuestionResDto(
                question.getId(),
                question.getContent(),
                question.getAnswer(),
                question.getTags(),
                question.getDomain());
    }
}
