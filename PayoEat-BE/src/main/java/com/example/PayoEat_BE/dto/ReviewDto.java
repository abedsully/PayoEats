package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private String reviewContent;
    private LocalDateTime createdAt;
}
