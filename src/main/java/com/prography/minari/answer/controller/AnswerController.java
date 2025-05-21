package com.prography.minari.answer.controller;

import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.answer.service.dto.SttConvertResponse;
import com.prography.minari.answer.service.dto.response.UserAnswerStatusResponse;
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

    // STT변환 API
    @PostMapping("/{userId}/questions/{questionId}")
    public ResponseEntity<Boolean> convertToText(@RequestParam("file") MultipartFile files,
                                                                  @PathVariable Long questionId,
                                                                  @PathVariable Long userId) {
        SttConvertResponse sttConvertResponse = answerService.writeUserSpeech(files, userId, questionId);
        return ResponseEntity.ok(sttConvertResponse.isSuccess());
    }


    // STT변환진행상태조회
    @GetMapping("/{userId}/questions/{questionId}/status")
    public ResponseEntity<UserAnswerStatusResponse> getAnswerStatus(@PathVariable Long questionId,
                                                                    @PathVariable Long userId) {
        UserAnswerStatusResponse statusResponse = answerService.getSttProcessStatus(userId, questionId);
        return ResponseEntity.ok(statusResponse);
    }
}
