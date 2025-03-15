package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.model.Transaction;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.AddTransactionRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.transaction.ITransactionService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequestMapping("/api/transaction")
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService transactionService;
    private final IUserService userService;

    @PostMapping("/top-up")
    @Operation(summary = "Add Top Up Transaction", description = "API for top up")
    public ResponseEntity<ApiResponse> topUp(@RequestBody AddTransactionRequest request) {
        try {
            User user = userService.getAuthenticatedUser();
            Transaction newTransaction = transactionService.addTopUpTransaction(request, user.getId());
            return ResponseEntity.ok(new ApiResponse("Top up is successfully made", newTransaction));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
