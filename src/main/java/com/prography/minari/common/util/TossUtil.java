package com.prography.minari.common.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class TossUtil {

    public static String READY = "READY";
    public static String IN_PROGRESS = "IN_PROGRESS";
    public static String WAITING_FOR_DEPOSIT = "WAITING_FOR_DEPOSIT";
    public static String DONE = "CANCELED";
    public static String CANCELED = "CANCELED";
    public static String PARTIAL_CANCELED = "PARTIAL_CANCELED";
    public static String ABORTED = "ABORTED";
    public static String EXPIRED = "EXPIRED";

    private static String AUTHORIZATION_PREFIX = "Basic ";

    public static String generateUUID(Long userId, Long productId) {
        return String.format("%s_%d_%d", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")), userId, productId);
    }

    public static String encodedSecret(String secret) {
        return AUTHORIZATION_PREFIX + Base64.getEncoder()
                .encodeToString((secret + ":").getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isGreaterThan(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(100_000)) > 0;
    }

}
