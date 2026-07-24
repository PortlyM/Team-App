package com.example.teamapp.user.services.impl;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " does not exist"));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
