package com.example.PayoEat_BE.request.menu;

import lombok.AllArgsConstructor;
import lombok.Data;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddMenuRequest {
    private String menuName;
    private String menuDetail;
    private Double menuPrice;
    private Boolean isActive;
}
