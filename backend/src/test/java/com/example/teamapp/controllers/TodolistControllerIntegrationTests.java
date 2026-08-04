package com.example.teamapp.controllers;

import com.example.teamapp.tasks.domain.dtos.CreateTodoListRequest;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static com.example.teamapp.util.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodolistControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @WithMockUser
    void createTodoList_ShouldReturnCreatedList() throws Exception{
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        CreateTodoListRequest createTodoListRequest = createRequestTodoList();

        mockMvc.perform(post("/api/v1/todolists/{teamid}", testTeam.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTodoListRequest))
        )
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.teamid", testTeam.getId()))
                .andExpect((ResultMatcher) jsonPath("$.name", createTodoListRequest.getName()))
                .andExpect((ResultMatcher) jsonPath("$.tasks").isEmpty());
    }
}
