package com.example.teamapp.user.controller;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.dtos.UserDto;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        User createdUser = userService.registerUser(registerUserRequest);
        UserDto savedUser = userMapper.toDto(createdUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
