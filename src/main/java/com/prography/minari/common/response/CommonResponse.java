package com.prography.minari.common.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private String code;
    private T result;

    public static <T> CommonResponse<T> success(T result) {
        return new CommonResponse<>("200", result);
    }

    public static CommonResponse ok() {
        return new CommonResponse<>("200", "request success");
    }
    public static <T> CommonResponse<T> fail(String code, T result) {
        return new CommonResponse<>(code, result);
    }

    private CommonResponse(String code, T result) {
        this.code = code;
        this.result = result;
    }

}
