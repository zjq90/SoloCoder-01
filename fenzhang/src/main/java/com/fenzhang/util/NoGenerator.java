package com.fenzhang.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class NoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    public static String generateProfitNo() {
        return "PRF" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    public static String generateDetailNo() {
        return "DTL" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    public static String generateLogNo() {
        return "LOG" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    public static String generateBatchNo() {
        return "BCH" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    public static String generateStatementNo() {
        return "STM" + LocalDateTime.now().format(FORMATTER) + getRandomSuffix();
    }

    private static String getRandomSuffix() {
        return String.valueOf(UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase());
    }
}
