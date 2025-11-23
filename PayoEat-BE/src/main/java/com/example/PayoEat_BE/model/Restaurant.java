package com.example.PayoEat_BE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Double rating;

    private Long totalRating;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isActive;

    private Long tax;

    private Long userId;

    private LocalTime openingHour;

    private LocalTime closingHour;

    private String location;

    private String telephoneNumber;

    private UUID restaurantImage;
    private String restaurantImageUrl;

    private UUID qrisImage;

    private String color;

    @Schema(description = "List of menu in restaurant")
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<Menu> menus;

    private Long restaurantCategory;

    public Restaurant(String name, Double rating, Long totalRating, String description, LocalTime openingHour, LocalTime closingHour, String location, String telephoneNumber, String color, Long tax, String restaurantImageUrl) {
        this.name = name;
        this.rating = rating;
        this.totalRating = totalRating;
        this.description = description;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.location = location;
        this.telephoneNumber = telephoneNumber;
        this.color = color;
        this.tax = tax;
        this.restaurantImageUrl = restaurantImageUrl;
    }
}
