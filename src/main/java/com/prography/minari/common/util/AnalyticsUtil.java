package com.prography.minari.common.util;

import com.prography.minari.answer.dto.res.AnswerResDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AnalyticsUtil {

    public static int calculateAchievementRate(List<AnswerResDto> answers, LocalDate startDate, LocalDate endDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (totalDays <= 0) return 0;

        long answeredCount = answers.stream()
                .filter(AnswerResDto::isExisted)
                .count();

        return toPercentage(answeredCount, totalDays);
    }

    public static int toPercentage(long numerator, long denominator) {
        if (denominator == 0) return 0;
        return Math.toIntExact(Math.round(numerator * 100.0 / denominator));
    }

}
