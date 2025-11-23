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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public void approveRestaurant(UUID id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found to approve this restaurant request"));

        if (!user.getRoleId().equals(1L)) {
            throw new ForbiddenException("Sorry only admin can approve restaurant");
        }


        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Restaurant approval not found with id: " + id));

        User userRestaurant = userRepository.findById(restaurantApproval.getUserId())
                .orElseThrow(() -> new NotFoundException("Restaurant user not found with id: " + restaurantApproval.getUserId()));

        if (!userRestaurant.getIsActive()) {
            throw new InvalidException("The user has not activated the email yet, you can't approve this restaurant yet");
        }


        if (restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()) {
            throw new InvalidException("This restaurant has been approved, Please ensure your approval id is right");
        } else if (!restaurantApproval.getIsApproved() && !restaurantApproval.getIsActive()){
            throw new InvalidException("This restaurant has been rejected, Please ensure your approval id is right");
        } else {
            UUID restaurantId = restaurantApproval.getRestaurantId();

            Restaurant restaurant = restaurantRepository.getDetail(restaurantId, Boolean.FALSE)
                    .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

            restaurantApproval.setIsApproved(true);
            restaurantApproval.setIsActive(false);

            restaurantRepository.approveRestaurant(restaurant.getId());
            restaurantApprovalRepository.addRestaurantApproval(restaurantApproval);
        }
    }

    @Override
    public void rejectRestaurant(RejectRestaurantRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found to approve this restaurant request"));

        if (!user.getRoleId().equals(1L)) {
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
            restaurantApprovalRepository.addRestaurantApproval(restaurantApproval);
        }
    }

    @Override
    public List<RestaurantApproval> getAllRestaurantApproval(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if(!user.getRoleId().equals(1L)) {
            throw new InvalidException("Sorry you cant do this");
        }
        return restaurantApprovalRepository.getAllRestaurantApproval();
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
