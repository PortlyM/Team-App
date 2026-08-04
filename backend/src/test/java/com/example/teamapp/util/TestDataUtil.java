package com.example.teamapp.util;

import com.example.teamapp.tasks.domain.TaskStatus;
import com.example.teamapp.tasks.domain.dtos.CreateTodoListRequest;
import com.example.teamapp.tasks.domain.enitities.Task;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataUtil {

    // --- USER ---

    //User with ID
    public static User createTestUser() {
        return createTestUser("test@example.com", "Test User");
    }

    public static User createTestUser(String email, String name) {
        User userWithId = createTestControllerUser(email, name);
        userWithId.setId(UUID.randomUUID());
        return userWithId;
    }

    //User without ID
    public static User createTestControllerUser() {
        return createTestControllerUser("test@example.com", "Test User");
    }

    public static User createTestControllerUser(String email, String name) {
        return User.builder()
                .email(email)
                .password("encodedPassword123")
                .name(name)
                .createdAt(LocalDateTime.now())
                .teamOwner(new ArrayList<>())
                .teamMember(new ArrayList<>())
                .build();
    }

    //Register User Request
    public static RegisterUserRequest createTestRegisterRequest() {
        return createTestRegisterRequest("testUser", "test@example.com");
    }

    public static RegisterUserRequest createTestRegisterRequest(String name, String email) {
        return RegisterUserRequest.builder()
                .name(name)
                .email(email)
                .password("testPassword")
                .build();
    }

    // --- TEAM ---

    //Team with ID
    public static Team createTestTeam(User leader) {
        return createTestTeam("Test Team", leader);
    }

    public static Team createTestTeam(String name, User leader) {
        Team teamWithId = createTestControllerTeam(name, leader);
        teamWithId.setId(UUID.randomUUID());
        return teamWithId;
    }

    //Team without ID
    public static Team createTestControllerTeam(User leader) {
        return createTestControllerTeam("Test Team", leader);
    }

    public static Team createTestControllerTeam(String name, User leader) {
        Team team = Team.builder()
                .name(name)
                .leader(leader)
                .todoLists(new ArrayList<>())
                .members(new ArrayList<>(List.of(leader)))
                .build();

        if (leader != null) {
            leader.getTeamOwner().add(team);
            leader.getTeamMember().add(team);
        }

        return team;
    }

    // --- TODOLIST ---

    //TodoList with ID
    public static TodoList createTestTodoList(Team team) {
        return createTestTodoList("Test TodoList", team);
    }

    public static TodoList createTestTodoList(String name, Team team) {
        TodoList todoListWithID = createTestControllerTodoList(name, team);
        todoListWithID.setId(UUID.randomUUID());
        return todoListWithID;
    }

    //TodoList without ID
    public static TodoList createTestControllerTodoList(Team team) {
        return createTestControllerTodoList("Test TodoList", team);
    }

    public static TodoList createTestControllerTodoList(String name, Team team) {
        TodoList todoList = TodoList.builder()
                .name(name)
                .team(team)
                .tasks(new ArrayList<>())
                .build();

        if (team != null) {
            team.getTodoLists().add(todoList);
        }

        return todoList;
    }

    //Create todo list request
    public static CreateTodoListRequest createRequestTodoList() {
        return createRequestTodoList("TestTodoList");
    }

    public static CreateTodoListRequest createRequestTodoList(String name) {
        return CreateTodoListRequest.builder()
                .name(name)
                .build();
    }

    // --- TASK ---

    //Task with ID
    public static Task createTestTask(TodoList list) {
        return createTestTask("Do something awesome", TaskStatus.NOT_STARTED, list);
    };

    public static Task createTestTask(String content, TaskStatus status, TodoList list) {
        Task taskWithID = createTestControllerTask(content, status, list);
        taskWithID.setId(UUID.randomUUID());
        return taskWithID;
    }

    //Task without ID
    public static Task createTestControllerTask(TodoList list) {
        return createTestControllerTask("Do something awesome", TaskStatus.NOT_STARTED, list);
    }

    public static Task createTestControllerTask(String content, TaskStatus status, TodoList list) {
        Task task = Task.builder()
                .content(content)
                .status(status)
                .list(list)
                .build();

        if (list != null) {
            list.getTasks().add(task);
        }

        return task;
    }
}
