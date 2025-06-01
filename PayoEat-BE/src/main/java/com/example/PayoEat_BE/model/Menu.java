package com.example.PayoEat_BE.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier of the menu")
    private UUID menuCode;

    @Schema(description = "Name of the menu")
    private String menuName;

    @Schema(description = "Detail of the menu")
    private String menuDetail;

    @Schema(description = "Price of the menu")
    private Double menuPrice;

    @Schema(description = "Date in which the restaurant is created at")
    private LocalDateTime createdAt;

    @Schema(description = "The latest date the restaurant is updated at")
    private LocalDateTime updatedAt;

    @Schema(description = "Status of the menu")
    private Boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private UUID menuImage;

    private String menuImageUrl;

    public Menu(String menuName, String menuDetail, double menuPrice) {
        this.menuName = menuName;
        this.menuDetail = menuDetail;
        this.menuPrice = menuPrice;
    }
}
