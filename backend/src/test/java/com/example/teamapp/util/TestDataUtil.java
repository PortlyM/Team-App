package com.example.teamapp.util;

import com.example.teamapp.tasks.domain.TaskStatus;
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

    public static User createTestUser() {
        return createTestUser("test@example.com", "Test User");
    }

    public static User createTestUser(String email, String name) {
        return User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encodedPassword123")
                .name(name)
                .createdAt(LocalDateTime.now())
                .teamOwner(new ArrayList<>())
                .teamMember(new ArrayList<>())
                .build();
    }

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

    public static Team createTestTeam(User leader) {
        return createTestTeam("Test Team", leader);
    }

    public static Team createTestTeam(String name, User leader) {
        Team team = Team.builder()
                .id(UUID.randomUUID())
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

    public static TodoList createTestTodoList(Team team) {
        return createTestTodoList("Test TodoList", team);
    }

    public static TodoList createTestTodoList(String name, Team team) {
        TodoList todoList = TodoList.builder()
                .id(UUID.randomUUID())
                .name(name)
                .team(team)
                .tasks(new ArrayList<>())
                .build();

        if (team != null) {
            team.getTodoLists().add(todoList);
        }

        return todoList;
    }

    // --- TASK ---

    public static Task createTestTask(TodoList list) {
        return createTestTask("Do something awesome", TaskStatus.NOT_STARTED, list);
    }

    public static Task createTestTask(String content, TaskStatus status, TodoList list) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
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
