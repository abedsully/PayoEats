package com.example.PayoEat_BE.request.menu;

import lombok.AllArgsConstructor;
import lombok.Data;


import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Long restaurantId;
}
