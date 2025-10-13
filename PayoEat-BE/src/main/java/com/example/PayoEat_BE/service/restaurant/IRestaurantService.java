package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IRestaurantService {
    Restaurant addRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImage, MultipartFile qrisImage);
    Restaurant updateRestaurant(UUID restaurantId, UpdateRestaurantRequest request);
    void deleteRestaurant(UUID restaurantId);
    Restaurant getRestaurantById(UUID id);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> findRestaurantByName(String name);
    RestaurantDto convertToDto(Restaurant restaurant);
    List<RestaurantDto> getConvertedRestaurants (List<Restaurant> restaurants);
    RestaurantApproval addRestaurantApproval(ReviewRestaurantRequest request);
    RestaurantApprovalDto convertApprovalToDto(RestaurantApproval restaurantApproval);
    Restaurant getRestaurantDetailForApproval(UUID id);
    List<Restaurant> getSimilarRestaurant(UUID id);
    String getRestaurantByUserId(Long userId);
}
