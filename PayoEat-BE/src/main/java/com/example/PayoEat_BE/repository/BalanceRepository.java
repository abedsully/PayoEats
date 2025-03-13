package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByUserId(Long userId);
}
