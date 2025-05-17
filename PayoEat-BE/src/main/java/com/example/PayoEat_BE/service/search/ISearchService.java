package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchResultDto;
import com.example.PayoEat_BE.model.Restaurant;

import java.util.List;

public interface ISearchService {
    List<SearchResultDto> search(String query);
    SearchResultDto convertToDto(Restaurant restaurant);
    List<SearchResultDto> getConvertedList(List<Restaurant> restaurantList);
}
