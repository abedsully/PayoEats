package com.example.PayoEat_BE.model;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private UUID id;
    private LocalDate createdDate;
    private LocalDateTime orderTime;
    private String orderMessage;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private UUID restaurantId;
    private String customerId;
    private LocalDateTime paymentBeginAt;
    private Double subTotal;
    private Double totalPrice;
    private Double taxPrice;
    private String cancellationReason;
    private LocalTime dineInTime;
    private String paymentImageUrl;
    private String paymentImageRejectionReason;
    private Long paymentImageRejectionCount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String customerName;
}
