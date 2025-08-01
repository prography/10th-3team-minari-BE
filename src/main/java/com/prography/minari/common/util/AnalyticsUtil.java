package com.prography.minari.common.util;

import com.prography.minari.answer.dto.res.AnswerResDto;
import com.prography.minari.answer.entity.Answer;

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

    public static int calculateConsecutiveDaysCount(Map<LocalDate, Answer> answers) {

        // 기준값(today) 선언
        LocalDate today = LocalDate.now();

        // 오늘 문제를 풀었다면 1일, 풀지 않았다면 0일부터 시작!
        int consecutiveDays = answers.containsKey(today) ? 1 : 0;

        // 오늘을 기준으로 1일전, 2일전, 3일전, ... 문제를 풀었다면 연속일수 값을 1 증가!
        for(LocalDate date = today.minusDays(1); answers.containsKey(date); date = date.minusDays(1)) {
            consecutiveDays++;
        }

        return consecutiveDays;
    }

}
