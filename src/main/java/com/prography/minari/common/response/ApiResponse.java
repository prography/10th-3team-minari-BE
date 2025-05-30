package com.prography.minari.common.response;

public class ApiResponse<T> {
    private String code;
    private T result;

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>("200", result);
    }

    public static <T> ApiResponse<T> fail(String code, T result) {
        return new ApiResponse<>(code, result);
    }

    private ApiResponse(String code, T result) {
        this.code = code;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public T getResult() {
        return result;
    }
}
