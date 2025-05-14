package com.prography.minari.answer.controller;

import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/{userId}/questions/{questionId}/answer")
    public ResponseEntity<InterviewContentResponse> getAnswer(@RequestParam("file") MultipartFile files,
                                                              @PathVariable Long questionId,
                                                              @PathVariable Long userId) {
        InterviewContentResponse interviewContentResponse = answerService.writeUserSpeech(files, userId, questionId);
        return ResponseEntity.ok(interviewContentResponse);
    }
}
