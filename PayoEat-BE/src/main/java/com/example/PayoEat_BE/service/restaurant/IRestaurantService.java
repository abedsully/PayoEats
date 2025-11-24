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
    UUID addRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImageUrl, MultipartFile qrisImageUrl);
    Restaurant getRestaurantById(UUID id);
    List<Restaurant> getAllRestaurants();
    RestaurantDto convertToDto(Restaurant restaurant);
    List<RestaurantDto> getConvertedRestaurants (List<Restaurant> restaurants);
    void addRestaurantApproval(UUID restaurantId);
    RestaurantApprovalDto convertApprovalToDto(RestaurantApproval restaurantApproval);
    Restaurant getRestaurantDetailForApproval(UUID id);
    List<Restaurant> getSimilarRestaurant(UUID id);
    UUID getRestaurantByUserId(Long userId);
}
