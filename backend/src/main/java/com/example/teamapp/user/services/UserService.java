package com.example.teamapp.user.services;

import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;

public interface UserService {
    User registerUser(RegisterUserRequest registerUserRequest);
}
