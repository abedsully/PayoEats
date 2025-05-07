package com.example.PayoEat_BE.service.user;


import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.model.VerificationToken;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.repository.VerificationTokenRepository;
import com.example.PayoEat_BE.request.menu.CreateUserRequest;
import com.example.PayoEat_BE.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {

        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email not valid");
        }

        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(null);
                    user.setActive(false);
                    user.setRoles(UserRoles.CUSTOMER);

                    User savedUser = userRepository.save(user);

                    String token = UUID.randomUUID().toString();
                    VerificationToken verificationToken = new VerificationToken();
                    verificationToken.setToken(token);
                    verificationToken.setUserId(savedUser.getId());
                    verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
                    tokenRepository.save(verificationToken);

                    emailService.sendConfirmationEmail(savedUser.getEmail(), token);

                    return savedUser;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Oops! " + request.getEmail() + " already exists!"));
    }

    public String confirmToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return "Invalid token.";
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token has expired.";
        }

        User user = userRepository.findById(verificationToken.getUserId())
                        .orElseThrow(() -> new NotFoundException("User not found with id: " + verificationToken.getUserId()));

        user.setActive(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return "User confirmed successfully.";
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }


}
