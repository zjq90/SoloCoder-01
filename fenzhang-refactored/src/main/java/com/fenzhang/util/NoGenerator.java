package com.fenzhang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class NoGenerator {

    private static final Logger logger = LoggerFactory.getLogger(NoGenerator.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final int MAX_COUNTER = 9999;

    private NoGenerator() {
    }

    public static String generateOrderNo() {
        return generateNo("ORD");
    }

    public static String generateProfitNo() {
        return generateNo("PRF");
    }

    public static String generateDetailNo() {
        return generateNo("DTL");
    }

    public static String generateLogNo() {
        return generateNo("LOG");
    }

    public static String generateBatchNo() {
        return generateNo("BCH");
    }

    public static String generateStatementNo() {
        return generateNo("STM");
    }

    private static String generateNo(String prefix) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        int sequence = COUNTER.getAndIncrement();
        if (sequence > MAX_COUNTER) {
            COUNTER.set(0);
            sequence = 0;
        }
        String no = prefix + timestamp + String.format("%04d", sequence);
        logger.debug("生成编号: {}", no);
        return no;
    }
}
