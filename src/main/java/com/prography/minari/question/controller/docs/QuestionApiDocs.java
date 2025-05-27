package com.prography.minari.question.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

public interface QuestionApiDocs {

    @Operation(
            summary = "질문 내용 조회",
            description = "질문 ID에 해당하는 질문의 내용을 문자열로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 내용 반환 성공"),
    })
    String getQuestion(
            @Parameter(description = "조회할 질문의 ID", required = true)
            @PathVariable Long questionId
    );
}
