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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate createdDate;
    private LocalTime createdTime;
    private String orderMessage;
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private UUID restaurantId;

    private LocalDateTime paymentBeginAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> menuLists = new ArrayList<>();

    // Sub total price dari items
    private Double subTotal;
    // Total price setelah ditambah subtotal & tax price
    private Double totalPrice;
    // Tax price diambil dari harga subTotal * tax price %
    private Double taxPrice;

    private UUID paymentImage;
    private String cancellationReason;
    private LocalTime dineInTime;
    private Long quotas;
    private String paymentImageRejectionReason;
    private Long paymentImageRejectionCount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
