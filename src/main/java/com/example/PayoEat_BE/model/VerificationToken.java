package com.example.PayoEat_BE.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VerificationToken {
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expiryDate;
    private Character type;
}