package com.prography.minari.answer.controller;

import com.prography.minari.answer.controller.docs.AnswerApiDocs;
import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.question.controller.dto.req.ConvertSttMemoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController implements AnswerApiDocs {
    private final AnswerService answerService;

    // STT변환 API
    @PostMapping("/{userId}/questions/{questionId}")
    public ResponseEntity<CommonResponse<String>> convertToText(@RequestPart("file") MultipartFile file,
                                                @PathVariable("questionId") Long questionId,
                                                @PathVariable("userId") Long userId,
                                                @RequestPart("request") ConvertSttMemoRequest request
    ) {
        answerService.writeUserSpeech(file, userId, questionId,request.getMemo());
        return ResponseEntity.ok(CommonResponse.ok());
    }

    // STT변환진행상태조회
    @GetMapping("/{userId}/questions/{questionId}/status")
    public ResponseEntity<CommonResponse<UserAnswerStatusResponse>> getAnswerStatus(@PathVariable Long questionId,
                                                                    @PathVariable Long userId) {
        UserAnswerStatusResponse statusResponse = answerService.getSttProcessStatus(userId, questionId);
        return ResponseEntity.ok(CommonResponse.success(statusResponse));
    }

    @GetMapping("/{userId}/questions/{questionId}")
    public ResponseEntity<CommonResponse<InterviewContentResponse>> getAnswer(@PathVariable("questionId") Long questionId,
                                                              @PathVariable("userId") Long userId){
        return ResponseEntity.ok(CommonResponse.success(answerService.getAnswer(questionId, userId)));
    }
}
