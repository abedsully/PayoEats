package com.example.PayoEat_BE.service.restaurantCategory;

import com.example.PayoEat_BE.model.RestaurantCategory;

import java.util.List;

public interface IRestaurantCategoryService {
    RestaurantCategory addCategory(String categoryName, Long userId);
    List<RestaurantCategory> getAllRestaurantCategory();
    RestaurantCategory getRestaurantCategoryById(Long id);
}
