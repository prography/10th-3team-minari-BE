package com.prography.minari.answer.controller;

import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.answer.service.dto.response.AnswerResponse;
import com.prography.minari.answer.service.impl.SttProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {
    private final SttProcessor sttProcessor;
    private final AnswerService answerService;

    @PostMapping("/{userId}/questions/{questionId}/answer")
    public ResponseEntity<AnswerResponse> getAnswer(@RequestParam("file") MultipartFile files,
                                                    @PathVariable Long questionId,
                                                    @PathVariable Long userId) {
        AnswerResponse answerResponse = answerService.writeUserSpeech(files, userId, questionId);
        return ResponseEntity.ok(answerResponse);
    }
}
