package com.example.PayoEat_BE.service.user;


import com.example.PayoEat_BE.exceptions.InvalidException;
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

import java.time.ZonedDateTime;
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
                .map(req -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setCreatedAt(ZonedDateTime.now());
                    user.setUpdatedAt(null);
                    user.setIsActive(false);
                    user.setRoleId(3L);

                    Long userId = userRepository.addUser(user);

                    String token = UUID.randomUUID().toString();
                    VerificationToken verificationToken = new VerificationToken();
                    verificationToken.setToken(token);
                    verificationToken.setUserId(userId);
                    verificationToken.setExpiryDate(ZonedDateTime.now().plusDays(1));
                    verificationToken.setType('1');
                    tokenRepository.add(verificationToken);

                    emailService.sendConfirmationEmail(request.getEmail(), token);

                    return user;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Oops! " + request.getEmail() + " already exists!"));
    }

    public String confirmToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByTokenAndType(token, '1');

        if (optionalToken.isEmpty()) {
            return "Invalid token.";
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
            return "Token has expired.";
        }

        User user = userRepository.findById(verificationToken.getUserId())
                        .orElseThrow(() -> new NotFoundException("User not found with id: " + verificationToken.getUserId()));

        user.setIsActive(true);
        userRepository.addUser(user);
        tokenRepository.delete(verificationToken.getId());

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

    @Override
    public String forgetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(user.getId());
        verificationToken.setExpiryDate(ZonedDateTime.now().plusDays(1));
        verificationToken.setType('1');

        tokenRepository.add(verificationToken);

        emailService.sendConfirmationEmail(user.getEmail(), token);

        return "Reset password link has been sent to your email, please check your email";
    }

    @Override
    public String resetPassword(String token, String password) {
        VerificationToken verificationToken = tokenRepository.findByTokenAndType(token, '2')
                .orElseThrow(() -> new NotFoundException("Token not found or invalid"));

        if (verificationToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
            throw new InvalidException("Token already expired");
        }

        User user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + verificationToken.getUserId()));

        user.setPassword(passwordEncoder.encode(password));

        userRepository.addUser(user);
        tokenRepository.delete(verificationToken.getId());

        return "Your password is changed successfully";

    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }


}
