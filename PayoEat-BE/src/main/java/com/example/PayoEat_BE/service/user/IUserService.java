package com.example.PayoEat_BE.service.user;


import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.CreateUserRequest;

import java.util.Optional;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User getAuthenticatedUser();
    User findByEmail(String email);
}
