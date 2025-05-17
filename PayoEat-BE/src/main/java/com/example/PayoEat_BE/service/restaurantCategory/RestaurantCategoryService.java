package com.example.PayoEat_BE.service.restaurantCategory;

import com.example.PayoEat_BE.dto.RestaurantCategoryDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.RestaurantCategory;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantCategoryRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryService implements IRestaurantCategoryService{
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
    public List<RestaurantCategoryDto> getAllRestaurantCategory() {
        List<RestaurantCategory> restaurantCategoryList =  restaurantCategoryRepository.findByIsActiveTrue();
        return getConvertedLists(restaurantCategoryList);
    }

    @Override
    public RestaurantCategory getRestaurantCategoryById(Long id) {
        return restaurantCategoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant categpry not found with id: " + id));

    }

    @Override
    public RestaurantCategoryDto convertToDto(RestaurantCategory restaurantCategory) {
        return modelMapper.map(restaurantCategory, RestaurantCategoryDto.class);
    }

    @Override
    public List<RestaurantCategoryDto> getConvertedLists(List<RestaurantCategory> restaurantCategoryList) {
        return restaurantCategoryList.stream().map(this::convertToDto).toList();
    }
}
