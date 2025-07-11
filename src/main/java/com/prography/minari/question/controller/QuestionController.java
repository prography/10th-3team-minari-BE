package com.prography.minari.question.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.question.controller.docs.QuestionApiDocs;
import com.prography.minari.question.dto.res.QuestionResDto;
import com.prography.minari.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse<String>> getQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(CommonResponse.success(questionService.readContents(questionId)));
    }

    @GetMapping("/users/{userId}/questions")
    public ResponseEntity<CommonResponse<Long>> getDailyQuestion(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(CommonResponse.success(questionService.readDaily(userId)));
    }

    @GetMapping("/questions/{questionId}/tag")
    public ResponseEntity<CommonResponse<List<String>>> getTags(@PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(CommonResponse.success(questionService.readTags(questionId)));
    }

    @GetMapping("/questions/{questionId}/tag/detail")
    public ResponseEntity<CommonResponse<String>> getTagDetail(@PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(CommonResponse.success(questionService.readTagDetail(questionId)));
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<CommonResponse<QuestionResDto>> getQuestionById(@PathVariable("questionId") Long questionId) {
        QuestionResDto dto = questionService.findById(questionId);
        return ResponseEntity.ok(CommonResponse.success(dto));
    }
}
