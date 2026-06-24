package com.example.teamapp.user.services.impl;

import com.example.teamapp.user.domain.UserEntity;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
