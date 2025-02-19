package com.example.PayoEat_Auth.service;

import com.example.PayoEat_Auth.model.User;
import com.example.PayoEat_Auth.request.CreateUserRequest;


public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User getAuthenticatedUser();
}
