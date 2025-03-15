package com.example.PayoEat_BE.model;

import com.example.PayoEat_BE.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;
    private TransactionType transactionType;
    private Double transactionAmount;
    private Long userId;
    private UUID orderId;
    private LocalDateTime dateTime;
}
