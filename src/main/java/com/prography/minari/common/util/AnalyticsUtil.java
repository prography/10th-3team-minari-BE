package com.prography.minari.common.util;

import com.prography.minari.answer.dto.res.AnswerResDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static int calculateConsecutiveDaysCount(List<AnswerResDto> answers, LocalDate startDate, LocalDate endDate) {

        LocalDate today = LocalDate.now();

        // 오늘 리허설을 진행했다면, 1 추가
        int consecutiveDays = answers.stream()
                .anyMatch(dto -> dto.answerDate().isEqual(today) && dto.isExisted()) ? 1 : 0;

        consecutiveDays += Math.toIntExact(
                answers.stream()
                        .filter(dto -> {
                            LocalDate date = dto.answerDate();
                            return !date.isBefore(startDate) && !date.isAfter(today.minusDays(1));
                        })
                        .sorted(Comparator.comparing(AnswerResDto::answerDate).reversed())
                        .map(AnswerResDto::isExisted)
                        .takeWhile(Boolean.TRUE::equals)
                        .count()
        );

        return consecutiveDays;
    }

}
