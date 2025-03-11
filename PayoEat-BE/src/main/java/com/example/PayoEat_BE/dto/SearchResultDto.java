package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.RestaurantCategory;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDto {
    private List<RestaurantCategory> restaurantCategoryList;
    private List<Menu> menuList;
}
