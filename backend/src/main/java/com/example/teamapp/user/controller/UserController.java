package com.example.teamapp.user.controller;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.dtos.UserDto;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        User createdUser = userService.registerUser(registerUserRequest);
        UserDto savedUser = userMapper.toDto(createdUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> listOfUsers = userService.getAllUsers();
        List<UserDto> listOfUsersDtos = userMapper.toDtoList(listOfUsers);
        return new ResponseEntity<>(listOfUsersDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/{userid}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userid) {
        User user = userService.getUser(userid);
        UserDto mappedUser = userMapper.toDto(user);
        return new ResponseEntity<>(mappedUser, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userid) {
        userService.deleteUser(userid);
        return ResponseEntity.noContent().build();
    }
}
