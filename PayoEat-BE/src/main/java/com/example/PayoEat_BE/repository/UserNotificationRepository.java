package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {
}
