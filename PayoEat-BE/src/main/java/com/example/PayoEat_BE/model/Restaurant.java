package com.example.PayoEat_BE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
    @Schema(description = "Unique identifier of the restaurant")
    private UUID id;

    @Schema(description = "Name of the restaurant")
    private String name;

    @Schema(description = "Rating of the restaurant")
    private Double rating;

    @Schema(description = "Description of the restaurant")
    private String description;

    @Schema(description = "Date in which the restaurant is created at")
    private LocalDateTime createdAt;

    @Schema(description = "The latest date the restaurant is updated at")
    private LocalDateTime updatedAt;

    @Schema(description = "Status of restaurant")
    private Boolean isActive;

    private Long userId;

    private LocalTime openingHour;

    private LocalTime closingHour;

    private String location;

    private String telephoneNumber;

    private Double taxFee;


    @Schema(description = "List of menu in restaurant")
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<Menu> menus;

    private Long restaurantCategory;

    public Restaurant(String name, Double rating, String description, LocalTime openingHour, LocalTime closingHour, String location, String telephoneNumber, Double taxFee) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.location = location;
        this.telephoneNumber = telephoneNumber;
        this.taxFee = taxFee;
    }
}
