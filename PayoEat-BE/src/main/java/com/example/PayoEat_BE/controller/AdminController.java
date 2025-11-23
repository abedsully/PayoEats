package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.admin.RejectRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.admin.IAdminService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "Endpoint for managing admin service")
public class AdminController {
    private final IAdminService adminService;
    private final IUserService userService;

    @PostMapping("/approve-restaurant")
    @Operation(summary = "Approving a restaurant approval request", description = "Endpoint for approving restaurant approval request")
    public ResponseEntity<ApiResponse> approveRestaurant(@RequestParam UUID id) {
        try {
            User user = userService.getAuthenticatedUser();
            adminService.approveRestaurant(id, user.getId());
            return ResponseEntity.ok(new ApiResponse("Restaurant is successfully approved", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reject-restaurant")
    @Operation(summary = "Rejecting a restaurant approval request", description = "Endpoint for rejecting restaurant approval request")
    public ResponseEntity<ApiResponse> rejectRestaurant(@RequestBody RejectRestaurantRequest request) {
        try {
            User user = userService.getAuthenticatedUser();
            adminService.rejectRestaurant(request, user.getId());
            return ResponseEntity.ok(new ApiResponse("Restaurant is successfully rejected", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get-approval")
    @Operation(summary = "Getting a restaurant approval request lists", description = "Endpoint for getting request approval list")
    public ResponseEntity<ApiResponse> getApprovalRequestLists() {
        try {
            User user = userService.getAuthenticatedUser();
            List<RestaurantApproval> approvalList = adminService.getAllRestaurantApproval(user.getId());
            List<RestaurantApprovalDto> convertedApprovalList = adminService.getConvertedApprovalDto(approvalList);
            return ResponseEntity.ok(new ApiResponse("Restaurant Approval Lists: ", convertedApprovalList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
