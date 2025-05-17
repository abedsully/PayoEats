package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SearchResultDto {
    private String restaurantName;
    private UUID restaurantId;
}
