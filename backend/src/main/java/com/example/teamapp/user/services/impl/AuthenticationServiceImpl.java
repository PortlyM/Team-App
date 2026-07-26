package com.example.teamapp.user.services.impl;

import com.example.teamapp.user.domain.dtos.AuthResponse;
import com.example.teamapp.user.domain.dtos.RefreshTokenRequest;
import com.example.teamapp.user.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private final Long jwtAccessExpiryMs = 86400000L;

    @Value("${jwt.refresh-token.expiration}")
    private final Long jwtRefreshExpiryMs = 86400000L;

    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtAccessExpiryMs);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtRefreshExpiryMs);
    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String username = extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (isTokenValid(refreshToken, userDetails)) {
            return AuthResponse.builder()
                    .token(generateAccessToken(userDetails))
                    .refreshToken(generateRefreshToken(userDetails))
                    .expiresIn(jwtAccessExpiryMs)
                    .build();
        }
        throw new RuntimeException("Token is invalid");
    }

    private String generateToken(UserDetails userDetails, Long expiryTime) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSingingKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired ", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Incorrect token signature ", e);
        } catch (Exception e) {
            throw new RuntimeException("Incorect token ", e);
        }
    }

    private SecretKey getSingingKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return userDetails.getUsername().equals(username);
    }
}
