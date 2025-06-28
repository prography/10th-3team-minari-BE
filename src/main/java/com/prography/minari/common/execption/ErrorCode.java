package com.prography.minari.common.execption;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력입니다.","C001"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다.","C002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.","C003"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.","C004"),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 회원가입이 완료된 사용자입니다.","C005"),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "남아있는 문제가 존재하지 않습니다.","Q001"),
    JWT_EXPIRED_EXCEPTION(HttpStatus.NOT_FOUND, "토큰이 만료되었습니다.","JWT001"),
    JWT_INVALID_SIGNATURE_EXCEPTION(HttpStatus.NOT_FOUND, "유효하지 않은 서명입니다.","JWT002"),
    JWT_UNSUPPORT_FORMAT_EXCEPTION(HttpStatus.NOT_FOUND, "지원하지 않는 JWT 포맷입니다.","JWT003"),
    JWT_WRONG_FORM_EXCEPTION(HttpStatus.NOT_FOUND, "잘못된 JWT 형식입니다.","JWT004"),
    JWT_EXCEPTION(HttpStatus.NOT_FOUND, "JWT 파싱 중 예상치 못한 상태 오류가 발생했습니다. 설정 또는 키 값이 올바른지 확인하세요.","JWT005"),
    JWT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "헤더에 JWT가 존재하는지 확인하세요.","JWT006"),
    JWT_NOT_MATCHED(HttpStatus.BAD_REQUEST, "유효하지 않은 REFRESH TOKEN입니다.","JWT007"),
    EMAIL_INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "잘못된 이메일 주소입니다.", "MAIL001"),
    EMAIL_MESSAGE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 메시지 생성 중 오류가 발생했습니다.", "MAIL002"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다.", "MAIL003"),
    METHOD_ARGUMENT_NOT_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "", "VALIDATION"),
    AUDIO_UNSUPPORT_FORMAT_EXCEPTION(HttpStatus.NOT_FOUND, "지원하지 않는 오디오 포맷입니다.","AU003"),
    REQUEST_BODY_IS_MISSING(HttpStatus.NOT_FOUND, "Request Body가 존재하지 않습니다.", "VALIDATION"),
    FREE_ANSWER_ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 오늘의 미나리에 답변하였습니다.", "A001"),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효햐지 않은 인증번호입니다.", "AUTH001"),
    EXPIRED_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다.", "AUTH002");

    private final HttpStatus status;
    private final String message;
    private final String code;

    ErrorCode(HttpStatus status, String message,String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}