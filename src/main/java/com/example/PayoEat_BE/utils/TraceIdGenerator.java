package com.example.PayoEat_BE.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TraceIdGenerator {

    public static String generateTraceId() {
        try {
            String datePart = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyMMdd"));

            String randomPart = UUID.randomUUID().toString();

            return datePart + "-" + randomPart;
        } catch (Exception e) {
            throw new RuntimeException("Error generating trace ID", e);
        }
    }
}
