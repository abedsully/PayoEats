package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.restaurant.AddRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;

import java.util.List;
import java.util.UUID;

public interface IRestaurantService {
    Restaurant addRestaurant(AddRestaurantRequest request, Long userId);
    Restaurant updateRestaurant(UUID restaurantId, UpdateRestaurantRequest request);
    void deleteRestaurant(UUID restaurantId);
    Restaurant getRestaurantById(UUID id);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> findRestaurantByName(String name);
    RestaurantDto convertToDto(Restaurant restaurant);
    List<RestaurantDto> getConvertedRestaurants (List<Restaurant> restaurants);
    RestaurantApproval addRestaurantApproval(ReviewRestaurantRequest request);
    RestaurantApprovalDto convertApprovalToDto(RestaurantApproval restaurantApproval);
}
