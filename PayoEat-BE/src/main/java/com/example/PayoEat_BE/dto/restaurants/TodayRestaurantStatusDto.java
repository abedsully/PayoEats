package com.example.PayoEat_BE.dto.restaurants;

import lombok.Data;

@Data
public class TodayRestaurantStatusDto {
    private Double totalPrice;
    private Long totalCount;
}
