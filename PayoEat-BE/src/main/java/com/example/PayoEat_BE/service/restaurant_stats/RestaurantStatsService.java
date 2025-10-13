package com.example.PayoEat_BE.service.restaurant_stats;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.RestaurantStatsRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantStatsService implements IRestaurantStatsService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantStatsRepository restaurantStatsRepository;

    @Override
    public TodayRestaurantStatsDto getTodayRestaurantStats(UUID restaurantId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        if (!user.getId().equals(restaurant.getUserId()) || user.getRoles() != UserRoles.RESTAURANT) {
            throw new ForbiddenException("Sorry you don't have access to view this order");
        }

        return restaurantStatsRepository.getTodayRestaurantStatus(restaurantId, LocalDate.now());
    }
}
