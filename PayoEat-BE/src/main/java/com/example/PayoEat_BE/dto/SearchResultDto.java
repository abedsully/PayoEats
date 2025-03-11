package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDto {
    private List<Restaurant> restaurantList;
    private List<Menu> menuList;
}
