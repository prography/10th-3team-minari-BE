package com.prography.minari.question.controller;

import com.prography.minari.common.response.ApiResponse;
import com.prography.minari.question.DailyUserQuestionResDto;
import com.prography.minari.question.controller.docs.QuestionApiDocs;
import com.prography.minari.question.service.QuestionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<DailyUserQuestionResDto> getDailyQuestion(@PathVariable("userId")Long userId){
        return ApiResponse.success(questionService.readDaily(userId));
    }
}
