package com.prography.minari.answer.dto.res;

import java.util.List;

public record AnswerHistoryResDto(
        Integer achievementRate,
        List<AnswerResDto> answerlist
) {
    public static AnswerHistoryResDto create(Integer achievementRate, List<AnswerResDto> answerlist) {
        return new AnswerHistoryResDto(achievementRate, answerlist);
    }
}
