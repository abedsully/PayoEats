package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;

import java.util.List;
import java.util.UUID;

public interface IAdminService {
    void approveRestaurant(UUID id);
    void rejectRestaurant(UUID id, String reason);
    List<RestaurantApproval> getAllRestaurantApproval(Long userId);
}
