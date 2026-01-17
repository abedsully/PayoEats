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

    private static final Long RESTAURANT_ROLE_ID = 2L;

    private final UserRepository userRepository;

    public User validateRestaurantUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!user.getRoleId().equals(RESTAURANT_ROLE_ID)) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        return user;
    }
}
