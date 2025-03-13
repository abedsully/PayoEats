package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.balance.IBalanceService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@Tag(name = "Balance Controller", description = "API for Managing User's Balance")
public class BalanceController {
    private final IBalanceService balanceService;
    private final IUserService userService;

    @PostMapping("/add-restaurant")
    @Operation(summary = "Add Restaurant", description = "Add restaurant by request")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESTAURANT')")
    public ResponseEntity<ApiResponse> getMyBalance() {
        try {
            User currentUser = userService.getAuthenticatedUser();
            Double userBalance = balanceService.getMyBalance(currentUser.getId());
            return ResponseEntity.ok(new ApiResponse("My balance: ", userBalance));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
