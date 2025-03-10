package com.example.PayoEat_BE.service.restaurantCategory;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.RestaurantCategory;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantCategoryRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryService implements IRestaurantCategoryService{
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final UserRepository userRepository;

    @Override
    public RestaurantCategory addCategory(String categoryName, Long userID) {

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userID));

        if (!user.getRoles().equals(UserRoles.ADMIN)) {
            throw new ForbiddenException("Only admin can add restaurant category");
        }

        if(restaurantCategoryRepository.existsByCategoryNameAndIsActiveTrue(categoryName)) {
            throw new InvalidException("Restaurant category already exist with name: " + categoryName);
        }

        RestaurantCategory newRestaurantCategory = new RestaurantCategory();
        newRestaurantCategory.setCategoryName(categoryName);
        newRestaurantCategory.setAddedAt(LocalDateTime.now());
        newRestaurantCategory.setIsActive(true);

        return restaurantCategoryRepository.save(newRestaurantCategory);
    }

    @Override
    public List<RestaurantCategory> getAllRestaurantCategory() {
        return restaurantCategoryRepository.findByIsActiveTrue();
    }
}
