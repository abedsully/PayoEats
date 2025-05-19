package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchMenuResultDto;
import com.example.PayoEat_BE.dto.SearchRestaurantResultDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SearchRestaurantResultDto> search(String query) {
        List<Restaurant> restaurantList = restaurantRepository.findByNameContainingIgnoreCase(query);

        return getConvertedRestaurant(restaurantList);
    }

    @Override
    public List<SearchMenuResultDto> searchMenu(UUID restaurantId, String query) {
        List<Menu> menuList = menuRepository.findByRestaurantIdAndMenuNameContainingIgnoreCaseAndIsActiveTrue(restaurantId, query);

        return getConvertedMenu(menuList);
    }

    @Override
    public SearchRestaurantResultDto convertRestaurantToDto(Restaurant restaurant) {
        return modelMapper.map(restaurant, SearchRestaurantResultDto.class);
    }

    @Override
    public List<SearchRestaurantResultDto> getConvertedRestaurant(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(this::convertRestaurantToDto).toList();
    }

    @Override
    public SearchMenuResultDto convertMenuToDto(Menu menu) {
        return modelMapper.map(menu, SearchMenuResultDto.class);
    }

    @Override
    public List<SearchMenuResultDto> getConvertedMenu(List<Menu> menuList) {
        return menuList.stream().map(this::convertMenuToDto).toList();
    }

}
