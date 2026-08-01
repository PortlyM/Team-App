package com.example.teamapp.controllers;

import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import static com.example.teamapp.util.TestDataUtil.createTestRegisterRequest;
import static org.hamcrest.Matchers.is;
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
                .andExpect((ResultMatcher) jsonPath("$.id").exists())
                .andExpect((ResultMatcher) jsonPath("$.name", is(registerUserRequest.getName())))
                .andExpect((ResultMatcher) jsonPath("$.email", is(registerUserRequest.getEmail())));
    }
}
