package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.OrderItem;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDetailDto {
    private String orderMessage;
    private UUID restaurantId;
    private List<OrderItem> menuLists;
    private Double totalAmount;
    private UUID paymentImage;
    private LocalDate createdDate;
    private LocalTime createdTime;
}
