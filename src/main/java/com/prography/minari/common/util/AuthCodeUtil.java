package com.prography.minari.common.util;

import java.util.Random;

public class AuthCodeUtil {
    private static final Random RANDOM = new Random();

    /**
     * 임의의 6자리 숫자(100000~999999)를 문자열로 반환합니다.
     * @return 6자리 숫자 문자열
     */
    public static String generate6DigitCode() {
        int code = 100000 + RANDOM.nextInt(900000); // 100000~999999
        return String.valueOf(code);
    }
}
