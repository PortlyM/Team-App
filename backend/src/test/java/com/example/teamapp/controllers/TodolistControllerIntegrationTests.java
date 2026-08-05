package com.example.teamapp.controllers;

import com.example.teamapp.tasks.domain.dtos.CreateTodoListRequest;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static com.example.teamapp.util.TestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodolistControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @WithMockUser
    void createTodoList_ShouldReturnCreatedList() throws Exception {
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
                .andExpect(jsonPath("$.teamId", is(testTeam.getId().toString())))
                .andExpect(jsonPath("$.name", is(createTodoListRequest.getName())))
                .andExpect(jsonPath("$.tasks").isEmpty());
    }

    @Test
    @WithMockUser
    void deleteTodoList_ShouldReturnNoContent() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList(testTeam);
        todoListRepository.save(testTodoList);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/api/v1/todolists/{listid}", testTodoList.getId())
        )
                .andExpect(status().isNoContent());

        boolean todoListExistsAfterDelete = todoListRepository.existsById(testTodoList.getId());
        assertFalse(todoListExistsAfterDelete, "TodoList has not been deleted");
    }

    @Test
    @WithMockUser
    void getAllTodoList_ShouldReturnEmptyList() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList("testTodolist", testTeam);
        TodoList testTodoList2 = createTestControllerTodoList("testTodolist2", testTeam);
        testTeam.getTodoLists().add(testTodoList);
        testTeam.getTodoLists().add(testTodoList2);

        mockMvc.perform(get("/api/v1/todolists/team/{teamid}", testTeam.getId())
        )
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(testTodoList.getName())))
                .andExpect(jsonPath("$[1].name", is(testTodoList2.getName())));
    }
}
