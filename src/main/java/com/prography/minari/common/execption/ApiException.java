package com.prography.minari.common.execption;

public class ApiException extends RuntimeException{

    private final String code;

    public ApiException(String code) {
        super();
        this.code = code;
    }

    public String getErrorCode() {
        return code;
    }

}
