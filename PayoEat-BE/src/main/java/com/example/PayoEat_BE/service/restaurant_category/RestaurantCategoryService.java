package com.example.PayoEat_BE.service.restaurant_category;

import com.example.PayoEat_BE.dto.RestaurantCategoryDto;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.RestaurantCategory;
import com.example.PayoEat_BE.repository.RestaurantCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryService implements IRestaurantCategoryService{
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final ModelMapper modelMapper;

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
