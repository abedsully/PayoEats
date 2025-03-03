package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;

public interface IAdminService {
    RestaurantApproval reviewRestaurant(ReviewRestaurantRequest request);
    void approveRestaurant(RestaurantApproval approvalRequest);
    void rejectRestaurant(RestaurantApproval approvalRequest, String rejectionMessage);
}
