package com.prography.minari.common.execption;

public class ApiException extends RuntimeException {

    private final String code;

    public ApiException(ErrorCode errorCode) {
        super();
        this.code = errorCode.getCode();
    }

    public String getErrorCode() {
        return code;
    }

}
