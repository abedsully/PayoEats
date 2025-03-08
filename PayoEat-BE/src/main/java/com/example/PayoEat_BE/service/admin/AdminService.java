package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.repository.RestaurantApprovalRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantApprovalRepository restaurantApprovalRepository;

    @Override
    public void approveRestaurant(UUID id) {
        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant approval not found with id: " + id));

        UUID restaurantId = restaurantApproval.getRestaurantId();

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveFalse(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.setIsActive(true);
        restaurantApproval.setIsApproved(true);
        restaurantApproval.setIsActive(false);

        restaurantRepository.save(restaurant);
        restaurantApprovalRepository.save(restaurantApproval);
    }

    @Override
    public void rejectRestaurant(UUID id, String reason) {
        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant Approval not found with id: " + id));

        restaurantApproval.setIsApproved(false);
        restaurantApproval.setReason(reason);

        restaurantApprovalRepository.save(restaurantApproval);
    }
}
