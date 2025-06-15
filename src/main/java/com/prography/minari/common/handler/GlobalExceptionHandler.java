package com.prography.minari.common.handler;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getErrorCode(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        // 첫 번째 오류만 반환하는 방식
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "유효성 검사에 실패했습니다.";

        return ResponseEntity.badRequest()
                .body(CommonResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALIDATION_EXCEPTION.getCode(), errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(ErrorCode.REQUEST_BODY_IS_MISSING.getCode(), ErrorCode.REQUEST_BODY_IS_MISSING.getMessage()));
    }

}
