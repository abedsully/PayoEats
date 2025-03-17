package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.exceptions.AlreadyExistException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.repository.RestaurantApprovalRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.restaurant.AddRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final RestaurantApprovalRepository restaurantApprovalRepository;
    private final IImageService imageService;

    @Override
    public Restaurant addRestaurant(AddRestaurantRequest request, Long userId, MultipartFile restaurantImage, MultipartFile qrisImage) {
        if (restaurantExists(request.getName())) {
            throw new AlreadyExistException(request.getName() + " already exists");
        }

        return restaurantRepository.save(createRestaurant(request, userId, restaurantImage, qrisImage));
    }

    @Override
    public Restaurant updateRestaurant(UUID restaurantId, UpdateRestaurantRequest request) {
        return restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .map(existingRestaurant -> updateExistingRestaurant(existingRestaurant, request))
                .map(restaurantRepository::save)
                .orElseThrow(() -> new NotFoundException("Restaurant not found!"));
    }

    private Restaurant updateExistingRestaurant(Restaurant existingRestaurant, UpdateRestaurantRequest request) {
        if ((request.getName() == null || request.getName().isEmpty()) &&
                (request.getDescription() == null || request.getDescription().isEmpty()) &&
                request.getRating() == null) {
            throw new IllegalArgumentException("No valid fields provided to update the restaurant.");
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            existingRestaurant.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            existingRestaurant.setDescription(request.getDescription());
        }

        if (request.getRating() != null) {
            existingRestaurant.setRating(request.getRating());
        }

        existingRestaurant.setUpdatedAt(LocalDateTime.now());

        return existingRestaurant;
    }

    @Override
    public void deleteRestaurant(UUID restaurantId) {
        restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .map(currentRestaurant -> {
                    deleteExistingRestaurant(currentRestaurant);
                    return restaurantRepository.save(currentRestaurant);
                })
                .orElseThrow(() -> new NotFoundException("Restaurant not found!"));
    }

    private void deleteExistingRestaurant(Restaurant existingRestaurant) {
        existingRestaurant.setUpdatedAt(LocalDateTime.now());
        existingRestaurant.setIsActive(false);
    }

    private boolean restaurantExists(String name) {
        return restaurantRepository.existsByNameAndIsActiveTrue(name);
    }

    private Restaurant createRestaurant(AddRestaurantRequest request, Long userId, MultipartFile restaurantImage, MultipartFile qrisImage) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidException("Name of restaurant cannot be empty");
        }

        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new InvalidException("Description of restaurant cannot be empty");
        }

        if (request.getOpeningHour() == null || request.getClosingHour() == null) {
            throw new InvalidException("Opening hour and closing hour cannot be null");
        }

        if (request.getOpeningHour().isAfter(request.getClosingHour())) {
            throw new InvalidException("Opening hour cannot be after closing hour");
        }

        if (request.getTelephoneNumber() == null || request.getTelephoneNumber().isEmpty()) {
            throw new InvalidException("Telephone number cannot be empty");
        }

        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new InvalidException("Location cannot be empty");
        }


        Restaurant restaurant = new Restaurant(
                request.getName(),
                0.0,
                request.getDescription(),
                request.getOpeningHour(),
                request.getClosingHour(),
                request.getLocation(),
                request.getTelephoneNumber(),
                request.getTaxFee()
        );

        restaurant.setLocation(request.getLocation());
        restaurant.setRestaurantCategory(request.getRestaurantCategory());
        restaurant.setUserId(userId);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(null);
        restaurant.setIsActive(false);

        Image image = imageService.saveRestaurantImage(restaurantImage, restaurant.getId());
        image.setRestaurantId(restaurant.getId());
        restaurant.setRestaurantImage(image.getId());

        Image imageQris = imageService.saveQrisImage(qrisImage, restaurant.getId());
        imageQris.setRestaurantId(restaurant.getId());
        restaurant.setQrisImage(imageQris.getId());

        return restaurant;
    }

    @Override
    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

    @Override
    public List<Restaurant> findRestaurantByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public RestaurantDto convertToDto(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public List<RestaurantDto> getConvertedRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream().map(this::convertToDto).toList();
    }

    @Override
    public RestaurantApproval addRestaurantApproval(ReviewRestaurantRequest request) {
        RestaurantApproval restaurantApproval = new RestaurantApproval();
        restaurantApproval.setRestaurantId(request.getRestaurantId());
        restaurantApproval.setUserId(request.getUserId());
        restaurantApproval.setRequestedAt(LocalDateTime.now());
        restaurantApproval.setIsApproved(false);
        restaurantApproval.setIsActive(true);


        restaurantApprovalRepository.save(restaurantApproval);

        return restaurantApproval;
    }

    @Override
    public RestaurantApprovalDto convertApprovalToDto(RestaurantApproval restaurantApproval) {
        return modelMapper.map(restaurantApproval, RestaurantApprovalDto.class);
    }


}
