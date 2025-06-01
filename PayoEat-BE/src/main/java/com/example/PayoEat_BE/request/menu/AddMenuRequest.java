package com.example.PayoEat_BE.request.menu;

import lombok.AllArgsConstructor;
import lombok.Data;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddMenuRequest {
    @JsonProperty("menuName")
    private String menuName;

    @JsonProperty("menuDetail")
    private String menuDetail;

    @JsonProperty("menuPrice")
    private Double menuPrice;

    @JsonProperty("restaurantId")
    private UUID restaurantId;

    private Long userId;

}
