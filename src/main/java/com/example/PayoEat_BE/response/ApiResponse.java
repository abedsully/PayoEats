package com.example.PayoEat_BE.response;

import com.example.PayoEat_BE.utils.TraceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class ApiResponse {
    private String responseMessage;
    private String traceId;
    private LocalDateTime timestamp;
    private Object result;

    public ApiResponse(String responseMessage, Object result) {
        this.responseMessage = responseMessage;
        this.result = result;
        this.traceId = TraceIdGenerator.generateTraceId();
        this.timestamp = LocalDateTime.now();
    }

    public static ApiResponse of(String responseMessage, Object result) {
        return new ApiResponse(responseMessage, result);
    }
}