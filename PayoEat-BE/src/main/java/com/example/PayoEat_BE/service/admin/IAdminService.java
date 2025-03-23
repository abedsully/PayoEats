package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.admin.RejectRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;

import java.util.List;
import java.util.UUID;

public interface IAdminService {
    RestaurantApproval approveRestaurant(UUID id, Long userId);
    RestaurantApproval rejectRestaurant(RejectRestaurantRequest request, Long userId);
    List<RestaurantApproval> getAllRestaurantApproval(Long userId);
    List<RestaurantApprovalDto> getConvertedApprovalDto(List<RestaurantApproval> approvalList);
    RestaurantApprovalDto convertToDto(RestaurantApproval restaurantApproval);
}
