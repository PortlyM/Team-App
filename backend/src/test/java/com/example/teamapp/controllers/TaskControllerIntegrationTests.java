package com.example.teamapp.controllers;

import com.example.teamapp.tasks.domain.TaskStatus;
import com.example.teamapp.tasks.domain.dtos.CreateTaskRequest;
import com.example.teamapp.tasks.domain.dtos.UpdateTaskStatus;
import com.example.teamapp.tasks.domain.enitities.Task;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TaskRepository;
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
import static com.example.teamapp.util.TestDataUtil.createTestControllerTodoList;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void createTask_ShouldReturnTaskAndCreatedStatus() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList(testTeam);
        todoListRepository.save(testTodoList);
        CreateTaskRequest testTaskRequest = createTestTaskRequest(testTodoList.getId());

        mockMvc.perform(post("/api/v1/todolists/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(testTaskRequest.getContent())));
    }

    @Test
    @WithMockUser
    void updateTask_ShouldReturnTaskAndOkStatus() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList(testTeam);
        todoListRepository.save(testTodoList);
        Task testTask = createTestControllerTask(testTodoList);
        taskRepository.save(testTask);
        UpdateTaskStatus updateTaskStatus = UpdateTaskStatus.builder().status(TaskStatus.IN_PROGRESS).build();

        mockMvc.perform(patch("/api/v1/todolists/tasks/{taskid}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskStatus))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(TaskStatus.IN_PROGRESS.toString())));
    }

    @Test
    @WithMockUser
    void deleteTask_ShouldReturnNoContent() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList(testTeam);
        todoListRepository.save(testTodoList);
        Task testTask = createTestControllerTask(testTodoList);
        taskRepository.save(testTask);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/api/v1/todolists/tasks/{taskid}", testTask.getId())
        )
                .andExpect(status().isNoContent());

        boolean taskHasBeenDeleted = taskRepository.existsById(testTask.getId());
        assertFalse(taskHasBeenDeleted, "Task has not been deleted");
    }

    @Test
    @WithMockUser
    void getTask_ShouldReturnTaskAndOkStatus() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);
        TodoList testTodoList = createTestControllerTodoList(testTeam);
        todoListRepository.save(testTodoList);
        Task testTask = createTestControllerTask(testTodoList);
        taskRepository.save(testTask);

        mockMvc.perform(get("/api/v1/todolists/tasks/{taskid}", testTask.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(testTask.getContent())));
    }
}
