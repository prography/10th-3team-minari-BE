package com.prography.minari.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TossUtil {

    public static String READY = "READY";
    public static String IN_PROGRESS = "IN_PROGRESS";
    public static String WAITING_FOR_DEPOSIT = "WAITING_FOR_DEPOSIT";
    public static String DONE = "CANCELED";
    public static String CANCELED = "CANCELED";
    public static String PARTIAL_CANCELED = "PARTIAL_CANCELED";
    public static String ABORTED = "ABORTED";
    public static String EXPIRED = "EXPIRED";

    public static String generateUUID(Long userId, Long productId) {
        return String.format("%s_%d_%d", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")), userId, productId);
    }

}
