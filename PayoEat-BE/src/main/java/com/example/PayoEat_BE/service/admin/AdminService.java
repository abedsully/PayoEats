package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminService implements IAdminService{
    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantApproval reviewRestaurant(ReviewRestaurantRequest request) {
        RestaurantApproval restaurantApproval = new RestaurantApproval();
        restaurantApproval.setRestaurant(request.getRestaurant());
        restaurantApproval.setUserId(request.getUserId());
        restaurantApproval.setRequestedAt(request.getRequestedAt());
        restaurantApproval.setIsApproved(false);
        restaurantApproval.setIsActive(true);

        return restaurantApproval;
    }

    @Override
    public void approveRestaurant(RestaurantApproval approvalRequest) {
        UUID restaurantId = approvalRequest.getRestaurant().getId();

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveFalse(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.setIsActive(true);
        approvalRequest.setIsApproved(true);
        approvalRequest.setIsActive(false);
    }

    @Override
    public void rejectRestaurant(RestaurantApproval approvalRequest, String rejectionMessage) {
        approvalRequest.setIsApproved(false);
        approvalRequest.setReason(rejectionMessage);
    }

}
