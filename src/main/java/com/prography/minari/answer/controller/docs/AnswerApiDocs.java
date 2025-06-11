package com.prography.minari.answer.controller.docs;

import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.question.controller.dto.req.ConvertSttMemoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface AnswerApiDocs {
    @Operation(summary = "음성 파일을 텍스트로 변환", description = "사용자가 업로드한 음성 파일을 STT로 변환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변환 성공"),
    })
    ResponseEntity<CommonResponse<String>> convertToText(
            @Parameter(description = "음성 파일") @RequestParam("file") MultipartFile file,
            @Parameter(description = "질문 ID") @PathVariable Long questionId,
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            @RequestBody ConvertSttMemoRequest request

            );

    @Operation(summary = "STT 변환상태 조회", description = "STT 변환 상태 또는 답변 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    ResponseEntity<CommonResponse<UserAnswerStatusResponse>> getAnswerStatus(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            @Parameter(description = "질문 ID") @PathVariable Long questionId
    );

    @Operation(
            summary = "사용자의 특정 질문 답변 조회",
            description = "사용자 ID와 질문 ID를 기반으로 해당 질문에 대한 문제,정답,사용자 답변을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 조회 성공",
                            content = @Content(schema = @Schema(implementation = CommonResponse.class))
                    ),
            }
    )
    @GetMapping("/{userId}/questions/{questionId}")
    ResponseEntity<CommonResponse<InterviewContentResponse>> getAnswer(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable("userId") Long userId,

            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId
    );
}
