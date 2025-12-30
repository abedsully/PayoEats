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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        String result = userService.confirmToken(token);

        String htmlResponse = """
        <html>
        <head>
            <meta http-equiv="refresh" content="5;url=http://localhost:5173/login" />
            <style>
                body { font-family: Arial, sans-serif; text-align: center; margin-top: 100px; }
                .message-box { padding: 20px; border: 1px solid #ccc; display: inline-block; border-radius: 10px; background-color: #f9f9f9; }
            </style>
        </head>
        <body>
            <div class="message-box">
                <h2>%s</h2>
                <p>You will be redirected to the login page shortly...</p>
            </div>
        </body>
        </html>
        """.formatted(result);

        return ResponseEntity.ok().body(htmlResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Endpoint for handling user's login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail());

            if (!user.getIsActive()) {
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

    @PostMapping("/forget-password")
    @Operation(summary = "Forget password user", description = "Endpoint for confirming email for forget password")
    public ResponseEntity<ApiResponse> forgetPassword(@RequestParam String email) {
        try {
            String message = userService.forgetPassword(email);
            return ResponseEntity.ok(new ApiResponse(message, null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", null));
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password user", description = "Endpoint for reseting password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String password, @RequestParam String token) {
        try {
            String message = userService.resetPassword(token, password);
            return ResponseEntity.ok(new ApiResponse(message, null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", null));
        }
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check email duplicate", description = "Endpoint for checking if email already exists")
    public ResponseEntity<ApiResponse> checkEmailExists(@RequestParam String email) {
        try {
            Boolean exists = userService.checkEmailExists(email);
            return ResponseEntity.ok(new ApiResponse("Check completed", exists));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", null));
        }
    }
}
