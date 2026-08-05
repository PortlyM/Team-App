package com.example.teamapp.controllers;

import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static com.example.teamapp.util.TestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_ShouldReturnCreatedStatus() throws Exception {
        RegisterUserRequest registerUserRequest = createTestRegisterRequest();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is(registerUserRequest.getName())))
                .andExpect(jsonPath("$.email", is(registerUserRequest.getEmail())));
    }

    @Test
    @WithMockUser
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        User testUser1 = createTestControllerUser("test1@example.com", "testUser1");
        User testUser2 = createTestControllerUser("test2@example.com", "testUser2");
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is(testUser1.getEmail())))
                .andExpect(jsonPath("$[1].email", is(testUser2.getEmail())));
    }

    @Test
    @WithMockUser
    void getUser_ShouldReturnUser() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);

        mockMvc.perform(get("/api/v1/users/{userid}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.name", is(testUser.getName())));
    }

    @Test
    @WithMockUser
    void deleteUser_ShouldReturnNoContent() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);

        mockMvc.perform(delete("/api/v1/users/{userid}", testUser.getId()))
                .andExpect(status().isNoContent());

        boolean userExistsAfterDelete = userRepository.existsById(testUser.getId());
        assertFalse(userExistsAfterDelete, "User has not been deleted");
    }
}
