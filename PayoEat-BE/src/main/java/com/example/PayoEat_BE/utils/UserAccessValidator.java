package com.example.PayoEat_BE.utils;

import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAccessValidator {

    private final UserRepository userRepository;

    public User validateRestaurantUser(Long userId) {
        // Role ID = 1 (Admin), 2 (Restaurant)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (user.getRoleId() != 2L) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        return user;
    }
}
