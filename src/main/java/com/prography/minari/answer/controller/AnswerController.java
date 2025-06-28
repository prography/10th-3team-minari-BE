package com.prography.minari.answer.controller;

import com.prography.minari.answer.controller.docs.AnswerApiDocs;
import com.prography.minari.answer.dto.req.InterviewSttConvertReq;
import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController implements AnswerApiDocs {
    private final AnswerService answerService;

    // STT변환 API
    @PostMapping("/{userId}/questions/{questionId}")
    public ResponseEntity<CommonResponse<InterviewContentResponse>> convertToText(
            @ModelAttribute InterviewSttConvertReq req,
            @PathVariable("questionId") Long questionId,
            @PathVariable("userId") Long userId
    ) {
        InterviewContentResponse interviewContentResponse = answerService.writeUserSpeech(
                req.getFile(),
                userId,
                questionId,
                req.getMemo());
        return ResponseEntity.ok(CommonResponse.success(interviewContentResponse));
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
                                                                              @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(CommonResponse.success(answerService.getAnswer(questionId, userId)));
    }

    @GetMapping("/answers")
    public ResponseEntity getAnswerList(
            @RequestParam("year") String year,
            @RequestParam("month") String month
            , @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(null);
    }
}
