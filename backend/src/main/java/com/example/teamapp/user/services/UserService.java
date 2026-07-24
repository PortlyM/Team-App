package com.example.teamapp.user.services;

import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User registerUser(RegisterUserRequest registerUserRequest);
    List<User> getAllUsers();
    User getUser(UUID userId);
    void deleteUser(UUID userId);
}
