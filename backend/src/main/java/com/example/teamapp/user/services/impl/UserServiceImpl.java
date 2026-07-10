package com.example.teamapp.user.services.impl;

import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
