package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchMenuResultDto;
import com.example.PayoEat_BE.dto.SearchRestaurantResultDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;

import java.util.List;
import java.util.UUID;

public interface ISearchService {
    List<Restaurant> search(String query);
    List<SearchMenuResultDto> searchMenu(UUID restaurantId, String query);
    SearchMenuResultDto convertMenuToDto(Menu menu);
    List<SearchMenuResultDto> getConvertedMenu(List<Menu> menuList);
}
