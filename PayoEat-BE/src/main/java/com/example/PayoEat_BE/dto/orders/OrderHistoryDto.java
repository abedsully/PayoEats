package com.example.PayoEat_BE.dto.orders;

import com.example.PayoEat_BE.dto.MenuListDto;
import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderHistoryDto {
    private UUID orderId;
    private LocalDate createdDate;
    private UUID restaurantId;
    private String restaurantName;
    private String customerName;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private Double subTotal;
    private Double totalPrice;
    private Long itemCount;
    private List<MenuListDto> menuLists;
}
