package com.example.PayoEat_BE.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    EMAIL_CONFIRMATION('1'),
    PASSWORD_RESET('2');

    private final char value;
}
