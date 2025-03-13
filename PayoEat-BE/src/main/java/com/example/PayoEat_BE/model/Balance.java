package com.example.PayoEat_BE.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "balance")
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    private Long userId;
    private Double balance;
    private LocalDateTime openedAt;
    private LocalDateTime lastTopUpAt;
    private Boolean isActive;
}
