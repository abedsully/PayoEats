package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class RestaurantOpenStatusDto {
    private UUID id;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private Boolean isOpen;
}
