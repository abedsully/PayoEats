package com.example.PayoEat_BE.service.restaurant_category;

import com.example.PayoEat_BE.dto.RestaurantCategoryDto;
import com.example.PayoEat_BE.model.RestaurantCategory;

import java.util.List;

public interface IRestaurantCategoryService {
    List<RestaurantCategoryDto> getAllRestaurantCategory();
    RestaurantCategory getRestaurantCategoryById(Long id);
    RestaurantCategoryDto convertToDto(RestaurantCategory restaurantCategory);
    List<RestaurantCategoryDto> getConvertedLists(List<RestaurantCategory> restaurantCategoryList);
}
