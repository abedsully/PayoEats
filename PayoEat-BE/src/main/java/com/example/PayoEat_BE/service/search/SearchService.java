package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchResultDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SearchResultDto> search(String query) {
        List<Restaurant> restaurantList = restaurantRepository.findByNameContainingIgnoreCase(query);

        return getConvertedList(restaurantList);
    }

    @Override
    public SearchResultDto convertToDto(Restaurant restaurant) {
        return modelMapper.map(restaurant, SearchResultDto.class);
    }

    @Override
    public List<SearchResultDto> getConvertedList(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(this::convertToDto).toList();
    }

    public List<SearchResultDto> findRelatedRestaurants(Restaurant selectedRestaurant) {
        List<Restaurant> related = restaurantRepository.findByNameContainingIgnoreCase(selectedRestaurant.getName());
        List<SearchResultDto> dtoList = related.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Ensure the selected restaurant is first, if found in the related list
        dtoList.removeIf(dto -> dto.getRestaurantId().equals(selectedRestaurant.getId()));
        dtoList.add(0, convertToDto(selectedRestaurant));

        // If no other matches, you might need logic here to fetch any remaining restaurants
        if (dtoList.size() <= 1) {
            List<Restaurant> anyOtherRestaurants = restaurantRepository.findAll();
            anyOtherRestaurants.stream()
                    .filter(r -> !r.getId().equals(selectedRestaurant.getId()))
                    .limit(5) // Limit the number of "any" restaurants
                    .forEach(r -> {
                        if (!dtoList.stream().anyMatch(dto -> dto.getRestaurantId().equals(r.getId()))) {
                            dtoList.add(convertToDto(r));
                        }
                    });
        }

        return dtoList;
    }

}
