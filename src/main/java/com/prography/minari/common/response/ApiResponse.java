package com.prography.minari.common.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200, "API 요청이 성공했습니다.", result);
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> fail(T result) {
        return new ApiResponse<>(400, "불가능한 요청입니다.", result);
    }

    public static ApiResponse<Void> fail() {
        return fail(null);
    }

    public static <T> ApiResponse<T> error(T result) {
        return new ApiResponse<>(500, "에러가 발생했습니다.", result);
    }

    public static <T> ApiResponse<T> unauthorized(T result) {
        return new ApiResponse<>(401, "인증되지 않은 클라이언트입니다.", result);
    }

    public static <T> ApiResponse<T> forbidden(T result) {
        return new ApiResponse<>(403, "권한이 없는 클라이언트입니다.", result);
    }

    public static ApiResponse<Void> error() {
        return error(null);
    }

    private ApiResponse(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
