package com.example.teamapp.user.services.impl;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User registerUser(RegisterUserRequest registerUserRequest) {
        User requestedRegister = userMapper.toEntity(registerUserRequest);
        requestedRegister.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        if (!userRepository.existsByEmail(registerUserRequest.getEmail())) {
            return userRepository.save(requestedRegister);
        }
        throw new EntityExistsException("User with email " + registerUserRequest.getEmail() + " already exists");
    }
}
