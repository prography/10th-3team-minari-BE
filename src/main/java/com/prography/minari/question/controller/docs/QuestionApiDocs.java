package com.prography.minari.question.controller.docs;

import com.prography.minari.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuestionApiDocs {

    @Operation(
            summary = "질문 내용 조회",
            description = "질문 ID에 해당하는 질문의 내용을 문자열로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "질문 내용 반환 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            )
    })
    @GetMapping("/questions/{questionId}/contents")
    ResponseEntity<CommonResponse<String>> getQuestion(
            @Parameter(description = "조회할 질문의 ID", required = true)
            @PathVariable Long questionId
    );

    @Operation(
            summary = "사용자의 오늘 질문 조회",
            description = "사용자 ID에 해당하는 오늘의 질문 내용을 문자열로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "오늘의 질문 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            )
    })
    @GetMapping("/users/{userId}/questions")
    ResponseEntity<CommonResponse<Long>> getDailyQuestion(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable("userId") Long userId
    );

    @Operation(
            summary = "질문에 연결된 태그 목록 조회",
            description = "질문 ID에 해당하는 태그 문자열 리스트를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "질문 태그 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            )
    })
    @GetMapping("/questions/{questionId}/tag")
    ResponseEntity<CommonResponse<List<String>>> getTags(
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId
    );
}
