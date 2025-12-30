package com.example.PayoEat_BE.service.user;


import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.CreateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User getAuthenticatedUser();
    User findByEmail(String email);
    String forgetPassword(String email);
    String resetPassword(String token, String password);
    Boolean checkEmailExists(String email);
}
