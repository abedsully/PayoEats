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
    private List<MenuListDto> menuLists;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime dineInTime;

    private LocalDateTime paymentBeginAt;
}
