package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.repository.RestaurantApprovalRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminService implements IAdminService{
    private final RestaurantRepository restaurantRepository;
    private final RestaurantApprovalRepository restaurantApprovalRepository;

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
    public void approveRestaurant(UUID id) {
        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant Approval not found with id: " + id));


        UUID restaurantId = restaurantApproval.getRestaurant().getId();

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveFalse(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.setIsActive(true);
        restaurantApproval.setIsApproved(true);
        restaurantApproval.setIsActive(false);
    }

    @Override
    public void rejectRestaurant(UUID id, String rejectionMessage) {

        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant Approval not found with id: " + id));

        restaurantApproval.setIsApproved(false);
        restaurantApproval.setReason(rejectionMessage);
    }

}
