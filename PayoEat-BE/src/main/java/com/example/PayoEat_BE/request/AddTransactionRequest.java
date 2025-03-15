package com.example.PayoEat_BE.request;

import com.example.PayoEat_BE.enums.TransactionType;
import lombok.Data;

import java.util.UUID;

@Data
public class AddTransactionRequest {
    private TransactionType transactionType;
    private UUID orderId;
    private Double transactionAmount;
}
