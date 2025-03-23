package com.example.PayoEat_BE.service.admin;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantApprovalRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.admin.RejectRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantApprovalRepository restaurantApprovalRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public RestaurantApproval approveRestaurant(UUID id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found to approve this restaurant request"));

        if (!user.getRoles().equals(UserRoles.ADMIN)) {
            throw new ForbiddenException("Sorry only admin can approve restaurant");
        }

        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Restaurant approval not found with id: " + id));

        if (restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()) {
            throw new InvalidException("This restaurant has been approved, Please ensure your approval id is right");
        } else if (!restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()){
            throw new InvalidException("This restaurant has been rejected, Please ensure your approval id is right");
        } else {
            UUID restaurantId = restaurantApproval.getRestaurantId();

            Restaurant restaurant = restaurantRepository.findByIdAndIsActiveFalse(restaurantId)
                    .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

            restaurant.setIsActive(true);
            restaurantApproval.setIsApproved(true);
            restaurantApproval.setIsActive(false);

            restaurantRepository.save(restaurant);
            return restaurantApprovalRepository.save(restaurantApproval);
        }
    }

    @Override
    public RestaurantApproval rejectRestaurant(RejectRestaurantRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found to approve this restaurant request"));

        if (!user.getRoles().equals(UserRoles.ADMIN)) {
            throw new ForbiddenException("Sorry only admin can approve restaurant");
        }

        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(request.getApprovalId())
                .orElseThrow(() -> new NotFoundException("Restaurant Approval not found with id: " + request.getApprovalId()));

        if (restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()) {
            throw new InvalidException("This restaurant has been approved, Please ensure your approval id is right");
        } else if (!restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()){
            throw new InvalidException("This restaurant has been rejected, Please ensure your approval id is right");
        } else {
            restaurantApproval.setIsApproved(false);
            restaurantApproval.setReason(request.getRejectionMessage());
            return restaurantApprovalRepository.save(restaurantApproval);
        }
    }

    @Override
    public List<RestaurantApproval> getAllRestaurantApproval(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if(!user.getRoles().equals(UserRoles.ADMIN)) {
            throw new InvalidException("Sorry you cant do this");
        }
        return restaurantApprovalRepository.findAll();
    }

    @Override
    public List<RestaurantApprovalDto> getConvertedApprovalDto(List<RestaurantApproval> approvalList) {
        return approvalList.stream().map(this::convertToDto).toList();
    }

    @Override
    public RestaurantApprovalDto convertToDto(RestaurantApproval restaurantApproval) {
        return modelMapper.map(restaurantApproval, RestaurantApprovalDto.class);
    }
}
