package com.prography.minari.common.execption;

public class ApiException extends RuntimeException {

    private final ErrorCode code;

    public ApiException(ErrorCode errorCode) {
        super();
        this.code = errorCode;
    }

    public String getErrorCode() {
        return code.getCode();
    }

    public String getErrorMessage() {
        return code.getMessage();
    }

}
