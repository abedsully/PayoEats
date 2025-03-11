package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchResultDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public SearchResultDto search(String query) {
        SearchResultDto result = new SearchResultDto();
        List<Menu> menuList = menuRepository.findByMenuNameContainingIgnoreCaseAndIsActiveTrue(query);
        List<Restaurant> restaurantList = restaurantRepository.findByNameContainingIgnoreCase(query);

        result.setMenuList(menuList);
        result.setRestaurantList(restaurantList);

        return result;
    }
}
