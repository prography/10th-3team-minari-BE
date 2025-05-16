package com.prography.minari.common.handler;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse handleApiException(ApiException e) {
        return ApiResponse.fail();
    }

}
