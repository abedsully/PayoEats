package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.RestaurantApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantApprovalRepository extends JpaRepository<RestaurantApproval, UUID> {
}
