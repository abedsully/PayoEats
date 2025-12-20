package com.example.PayoEat_BE.service.restaurant;

import com.example.PayoEat_BE.dto.restaurants.CheckUserRestaurantDto;
import com.example.PayoEat_BE.enums.UploadType;
import com.example.PayoEat_BE.exceptions.AlreadyExistException;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.repository.*;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.service.EmailService;
import com.example.PayoEat_BE.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.example.PayoEat_BE.utils.EmailValidation.isValidEmail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final RestaurantRepository restaurantRepository;
    private final UploadService uploadService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);

    @Override
    public UUID addRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImageUrl, MultipartFile qrisImageUrl) {
        if (restaurantExists(request.getRestaurantName())) {
            throw new AlreadyExistException(request.getRestaurantName() + " already exists");
        }

        if (!request.getRoleId().equals(2L)) {
            throw new ForbiddenException("Sorry you can't create a restaurant with that role");
        }

        return createRestaurant(request, restaurantImageUrl, qrisImageUrl);
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

        existingRestaurant.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Jakarta")));

        return existingRestaurant;
    }

    private void deleteExistingRestaurant(Restaurant existingRestaurant) {
        existingRestaurant.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Jakarta")));
        existingRestaurant.setIsActive(false);
    }

    private boolean restaurantExists(String name) {
        return restaurantRepository.existsByNameAndIsActiveTrue(name);
    }

    private UUID createRestaurant(RegisterRestaurantRequest request, MultipartFile restaurantImageUrl, MultipartFile qrisImageUrl) {
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


        User user = new User();
        user.setRoleId(request.getRoleId());
        if (request.getRoleId().equals(2L)) {
            user.setUsername(null);
        }
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Jakarta")));
        user.setUpdatedAt(null);
        user.setIsActive(false);

        Long userId = userRepository.addUser(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(userId);
        verificationToken.setExpiryDate(LocalDateTime.now(ZoneId.of("Asia/Jakarta")).plusDays(1));
        verificationToken.setType('1');
        verificationTokenRepository.add(verificationToken);

        emailService.sendConfirmationEmail(request.getEmail(), token);

        String url1 = uploadService.upload(restaurantImageUrl, UploadType.RESTAURANT);
        String url2 = uploadService.upload(qrisImageUrl, UploadType.QR);

        return restaurantRepository.addRestaurant(request, userId, url1, url2);
    }

    @Override
    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.getDetail(id, Boolean.TRUE)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.getAllRestaurant();
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
    public List<Restaurant> getSimilarRestaurant(UUID id) {
        checkIfRestaurantExists(id);

        Long restaurantCategoryCode = restaurantRepository.getRestaurantCategory(id);

        return restaurantRepository.getSimilarRestaurant(restaurantCategoryCode, id);
    }

    @Override
    public UUID getRestaurantByUserId(Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);
        return restaurantRepository.getRestaurantId(user.getUserId());
    }



    private void checkIfRestaurantExists(UUID restaurantId) {
        restaurantRepository.findRestaurantByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant is not found"));
    }

    private CheckUserRestaurantDto checkUserRestaurant(Long userId) {
        CheckUserRestaurantDto result = restaurantRepository.checkUserRestaurant(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        if (result.getRoleId() != 2L || !result.getUserId().equals(userId)) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        return result;
    }




}
