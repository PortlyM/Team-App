package com.example.teamapp.services;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.teamapp.util.TestDataUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_ShouldReturnSavedUser_WhenUserDoesNotExist() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("plainPassword");

        User mappedUser = new User();
        mappedUser.setEmail("test@example.com");

        User savedUser = createTestUser("test@example.com", "Test Name");
        savedUser.setPassword("encodedPassword");

        when(userMapper.toEntity(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void registerUser_ShouldThrowEntityExistsException_WhenEmailIsAlreadyTaken() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("taken@example.com");
        request.setPassword("somePassword");
        
        User mappedUser = new User();
        mappedUser.setEmail("taken@example.com");

        when(userMapper.toEntity(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode("somePassword")).thenReturn("encodedPassword");
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class, 
                () -> userService.registerUser(request)
        );

        assertEquals("User with email taken@example.com already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        User user1 = createTestUser("user1@test.com", "User 1");
        User user2 = createTestUser("user2@test.com", "User 2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@test.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        User existingUser = createTestUser("test@example.com", "Test Name");
        UUID userId = existingUser.getId();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUser_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        UUID nonExistingId = UUID.randomUUID();
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, 
                () -> userService.getUser(nonExistingId)
        );

        assertEquals("User with id " + nonExistingId + " does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void deleteUser_ShouldCallDeleteById() {
        UUID userId = UUID.randomUUID();

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
