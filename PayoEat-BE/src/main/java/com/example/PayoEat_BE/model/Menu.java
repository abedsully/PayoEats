package com.example.PayoEat_BE.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private UUID menuCode;
    private String menuName;
    private String menuDetail;
    private Double menuPrice;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean isActive;
    private UUID restaurantId;
    private String menuImageUrl;
}
