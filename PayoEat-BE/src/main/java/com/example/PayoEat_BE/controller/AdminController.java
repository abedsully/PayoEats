package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.admin.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "Endpoint for managing admin service")
public class AdminController {
    private final IAdminService adminService;

    @PostMapping("/approve-restaurant/{id}")
    @Operation(summary = "Approving a restaurant approval request", description = "Endpoint for approving restaurant approval request")
    public ResponseEntity<ApiResponse> approveRestaurant(@PathVariable UUID id) {
        try {
            adminService.approveRestaurant(id);
            return ResponseEntity.ok(new ApiResponse("Restaurant is approved", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reject-restaurant/{id}")
    @Operation(summary = "Rejecting a restaurant approval request", description = "Endpoint for rejecting restaurant approval request")
    public ResponseEntity<ApiResponse> rejectRestaurant(@PathVariable UUID id, @RequestParam String reason) {
        try {
            adminService.rejectRestaurant(id, reason);
            return ResponseEntity.ok(new ApiResponse("Restaurant is rejected", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }
}
