package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.AlreadyExistException;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.repository.RestaurantApprovalRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.repository.VerificationTokenRepository;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.service.EmailService;
import com.example.PayoEat_BE.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static com.example.PayoEat_BE.utils.EmailValidation.isValidEmail;

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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);

    @Override
    public Restaurant addRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImage, MultipartFile qrisImage) {
        if (restaurantExists(request.getRestaurantName())) {
            throw new AlreadyExistException(request.getRestaurantName() + " already exists");
        }

        if (!request.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("Sorry you can't create a restaurant with this roles: " + request.getRoles());
        }

        Restaurant newRestaurant = restaurantRepository.save(createRestaurant(request, restaurantImage, qrisImage));

        if (restaurantRepository.existsByUserIdAndIsActiveTrue(newRestaurant.getUserId())) {
            throw new ForbiddenException("Sorry you have already created a restaurant");
        }

        return newRestaurant;
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

    private Restaurant createRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImage, MultipartFile qrisImage) {
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email not valid");
        }

        if (request.getRestaurantName() == null || request.getRestaurantName().isEmpty()) {
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

        if (request.getColor() == null || request.getColor().isEmpty()) {
            throw new InvalidException("Color cannot be empty");
        }


        Restaurant restaurant = new Restaurant(
                request.getRestaurantName(),
                0.0,
                request.getDescription(),
                request.getOpeningHour(),
                request.getClosingHour(),
                request.getLocation(),
                request.getTelephoneNumber(),
                request.getColor()
        );

        User user = new User();
        user.setRoles(request.getRoles());
        if (request.getRoles().equals(UserRoles.RESTAURANT)) {
            user.setUsername(null);
            user.setRestaurantName(request.getRestaurantName());
        }
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setActive(true);

        User savedUser = userRepository.save(user);


        restaurant.setLocation(request.getLocation());
        restaurant.setRestaurantCategory(request.getRestaurantCategory());
        restaurant.setUserId(user.getId());
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(null);
        restaurant.setIsActive(false);

        Image image = imageService.saveRestaurantImage(restaurantImage, restaurant.getId());
        image.setRestaurantId(restaurant.getId());
        restaurant.setRestaurantImage(image.getId());

        Image imageQris = imageService.saveQrisImage(qrisImage, restaurant.getId());
        imageQris.setRestaurantId(restaurant.getId());
        restaurant.setQrisImage(imageQris.getId());


        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(savedUser.getId());
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationToken.setType('1');
        verificationTokenRepository.save(verificationToken);

        emailService.sendConfirmationEmail(savedUser.getEmail(), token);

        return restaurant;
    }

    @Override
    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        LOGGER.info("Masuk request all restaurants");

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
        restaurantApproval.setRestaurantName(request.getRestaurantName());
        restaurantApproval.setRestaurantImage(request.getRestaurantImage());
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

    @Override
    public Restaurant getRestaurantDetailForApproval(UUID id) {
        return restaurantRepository.findByIdAndIsActiveFalse(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public List<Restaurant> getSimilarRestaurant(UUID id) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));

        return restaurantRepository.findByRestaurantCategoryAndIsActiveTrueAndIdNot(restaurant.getRestaurantCategory(), id);
    }


}
