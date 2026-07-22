package com.example.teamapp.teams.domain.dtos;

import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamDto {

    private UUID id;
    private String name;
    private User leader;
    private List<TodoList> todoLists = new ArrayList<>();
    private List<User> members = new ArrayList<>();
}
