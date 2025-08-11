package com.prography.minari.common.handler;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.execption.TossApiException;
import com.prography.minari.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.ConnectException;

import static com.prography.minari.common.execption.ErrorCode.REQUEST_PARAM_IS_MISSING;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        log.info(e.getErrorMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(TossApiException.class)
    public ResponseEntity handleTossApiException(TossApiException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getCode(), e.getMessage()));
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

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> handleConnectException(ConnectException e) {
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(ErrorCode.DB_CONNECTION_ERROR.getCode(), ErrorCode.DB_CONNECTION_ERROR.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse> handleMissing(MissingServletRequestParameterException ex) {
        String message = String.format(
                "Parameter '%s' is missing and should be of type %s",
                ex.getParameterName(),
                ex.getParameterType() != null ? ex.getParameterType() : "unknown"
        );
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(REQUEST_PARAM_IS_MISSING.getCode(), message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(REQUEST_PARAM_IS_MISSING.getCode(), message));
    }


}
