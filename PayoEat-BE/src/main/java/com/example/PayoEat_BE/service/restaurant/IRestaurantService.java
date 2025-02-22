package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.request.restaurant.AddRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;

import java.util.List;

public interface IRestaurantService {
    Restaurant addRestaurant(AddRestaurantRequest request, Long userId);
    Restaurant updateRestaurant(Long restaurantId, UpdateRestaurantRequest request);
    void deleteRestaurant(Long restaurantId);
    Restaurant getRestaurantById(Long id);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> findRestaurantByName(String name);
    RestaurantDto convertToDto(Restaurant restaurant);
    List<RestaurantDto> getConvertedRestaurants (List<Restaurant> restaurants);
}
