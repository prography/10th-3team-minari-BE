package com.prography.minari.common.handler;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(e.getErrorCode()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("토큰이 만료되었습니다.");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity handleSignatureException(SignatureException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 서명입니다.");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity handleUnsupportedJwtException(UnsupportedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("지원하지 않는 JWT 포맷입니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("잘못된 JWT 형식입니다.");
    }

}
