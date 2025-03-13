package com.example.PayoEat_BE.service.balance;

import com.example.PayoEat_BE.model.Balance;

import java.util.UUID;

public interface IBalanceService {
    Balance activateBalance(Long userId);
    Double getMyBalance(Long userId);
    void addBalance(Double amount, Long userId);
    void processPayment(Double amount, UUID restaurantId, Long userId);
}
