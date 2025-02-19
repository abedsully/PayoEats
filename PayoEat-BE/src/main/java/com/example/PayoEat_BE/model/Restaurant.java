package com.example.PayoEat_BE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the restaurant")
    private Long id;

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

    @Schema(description = "List of menu in restaurant")
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<Menu> menus;

    public Restaurant(String name, Double rating, String description) {
        this.name = name;
        this.rating = rating;
        this.description = description;
    }
}
