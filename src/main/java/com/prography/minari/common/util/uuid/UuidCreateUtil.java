package com.prography.minari.common.util.uuid;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UuidCreateUtil {
    private static final char[] ALLOWED =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    private static final int FIXED_LEN = 64;

    public static String createTossUUIDKeyLongToString(long value,int len) {
        try {
            // 1. long -> byte[]
            byte[] inputBytes = ByteBuffer.allocate(Long.BYTES).putLong(value).array();

            // 2. SHA-256 해시
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(inputBytes);

            // 3. 해시 바이트를 허용 문자 집합으로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                int unsigned = b & 0xFF;
                sb.append(ALLOWED[unsigned % ALLOWED.length]);
            }

            // 4. 64자 만들기 (32바이트 해시 -> 32문자이므로, 2번 반복)
            while (sb.length() < len) {
                sb.append(sb);
            }
            return sb.substring(0, len);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
