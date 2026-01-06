package com.example.PayoEat_BE.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class ActiveOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private String orderStatus;
    private String customerName;
    private List<MenuListDto> menuLists;
    private String orderMessage;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime dineInTime;

    private LocalDateTime paymentBeginAt;

    private LocalDateTime paymentConfirmedAt;

    private LocalDateTime scheduledCheckInTime;

    private LocalDateTime checkInExpiredAt;
}
