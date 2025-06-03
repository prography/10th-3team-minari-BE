package com.prography.minari.question.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.question.dto.DailyUserQuestionResDto;
import com.prography.minari.question.controller.docs.QuestionApiDocs;
import com.prography.minari.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController implements QuestionApiDocs {
    private final QuestionService questionService;

    @GetMapping("/questions/{questionId}/contents")
    public String getQuestion(@PathVariable Long questionId) {
        return questionService.readContents(questionId);
    }

    @GetMapping("/users/{userId}/questions")
    public CommonResponse<DailyUserQuestionResDto> getDailyQuestion(@PathVariable("userId") Long userId) {
        return CommonResponse.success(questionService.readDaily(userId));
    }

    @GetMapping("/questions/{questionId}/tag")
    public CommonResponse<List<String>> getTags(@PathVariable("questionId") Long questionId) {
        return CommonResponse.success(questionService.readTags(questionId));
    }
}
