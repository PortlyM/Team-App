package com.example.teamapp.user.controller;

import com.example.teamapp.user.domain.dtos.AuthResponse;
import com.example.teamapp.user.domain.dtos.LoginRequest;
import com.example.teamapp.user.domain.dtos.RefreshTokenRequest;
import com.example.teamapp.user.services.AuthenticationService;
import com.example.teamapp.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String accessToken = authenticationService.generateAccessToken(userDetails);
        String refreshToken = authenticationService.generateRefreshToken(userDetails);
        UUID userId = userService.getUserByEmail(loginRequest.getEmail()).getId();
        AuthResponse authResponse = AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(86400000L)
                .userId(userId)
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse response = authenticationService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
