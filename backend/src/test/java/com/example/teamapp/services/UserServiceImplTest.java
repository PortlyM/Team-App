package com.example.teamapp.services;

import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import com.example.teamapp.user.services.impl.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.teamapp.util.TestDataUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.*;
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
    void testRegisterUser_ShouldReturnSavedUser_WhenNoUserExist() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName("Tets User");
        registerUserRequest.setEmail("test@example.com");
        registerUserRequest.setPassword("testpassword");

        User mappedUser = createTestUser();

        User savedUser = createTestUser("test@example.com", "Test User");
        savedUser.setPassword("encodedPassword");

        when(userMapper.toEntity(registerUserRequest)).thenReturn(mappedUser);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedPassword");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(mappedUser)).thenReturn(savedUser);

        User result = userService.registerUser(registerUserRequest);

        assertNotNull(result);
        assertEquals(result.getId(), savedUser.getId());
        assertEquals(result.getName(), savedUser.getName());
        assertEquals(result.getPassword(), savedUser.getPassword());

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void testRegisterUser_ShouldThrowException_WhenUserWithGivenEmailExists() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName("Tets User");
        registerUserRequest.setEmail("test@example.com");
        registerUserRequest.setPassword("testpassword");

        User mappedUser = createTestUser();

        when(userMapper.toEntity(registerUserRequest)).thenReturn(mappedUser);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedPassword");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> userService.registerUser(registerUserRequest)
        );

        assertEquals("User with email test@example.com already exists", exception.getMessage());
        verify(userRepository, never()).save(mappedUser);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        User testUser1 = createTestUser();
        User testUser2 = createTestUser("test2@example.com", "testUser2");
        List<User> listOfUsers = List.of(testUser2, testUser1);

        when(userRepository.findAll()).thenReturn(listOfUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(result.size(), listOfUsers.size());
        assertEquals(result, listOfUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        User returneduser = createTestUser();

        when(userRepository.findById(returneduser.getId())).thenReturn(Optional.of(returneduser));

        User result = userService.getUser(returneduser.getId());

        assertEquals(result.getEmail(), returneduser.getEmail());
        assertEquals(result.getPassword(), returneduser.getPassword());

        verify(userRepository, times(1)).findById(returneduser.getId());
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotExist() {
        UUID uuid = UUID.randomUUID();

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUser(uuid)
        );

        assertEquals(exception.getMessage(), "User with id " + uuid + " does not exist");

        verify(userRepository, times(1)).findById(uuid);
    }
}
