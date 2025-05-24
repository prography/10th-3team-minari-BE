package com.prography.minari.common.response;

public class ApiResponse<T> {
    private Integer code;
    private T result;

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200, result);
    }

    public static <T> ApiResponse<T> fail(Integer code) {
        return new ApiResponse<>(code, null);
    }

    private ApiResponse(Integer code, T result) {
        this.code = code;
        this.result = result;
    }

}
