package com.example.teamapp.user.services;

import com.example.teamapp.user.domain.dtos.AuthResponse;
import com.example.teamapp.user.domain.dtos.RefreshTokenRequest;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    UserDetails validateToken(String token);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
