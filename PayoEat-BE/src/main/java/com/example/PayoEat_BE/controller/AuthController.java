package com.example.PayoEat_BE.controller;


import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.CreateUserRequest;
import com.example.PayoEat_BE.request.menu.LoginRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.response.JwtResponse;
import com.example.PayoEat_BE.security.jwt.JwtUtils;
import com.example.PayoEat_BE.security.user.AuthUserDetails;
import com.example.PayoEat_BE.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "Endpoint for managing restaurants")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Endpoint for registering user")
    public ResponseEntity<ApiResponse> register(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", user));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/confirm")
    @Operation(summary = "User email confirmation", description = "Endpoint for confirming registered user's email")
    public String confirm(@RequestParam String token) {
        return userService.confirmToken(token);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Endpoint for handling user's login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail());

            if (!user.isActive()) {
                return ResponseEntity.status(UNAUTHORIZED)
                        .body(new ApiResponse("Account not activated. Please confirm your email.", null));
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/user")
    @Operation(summary = "Getting authenticated user", description = "Endpoint for getting the currently authenticated user")
    public ResponseEntity<ApiResponse> getAuthenticatedUser() {
        try {
            User user = userService.getAuthenticatedUser();
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (Exception e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("Fail", null));
        }
    }
}
