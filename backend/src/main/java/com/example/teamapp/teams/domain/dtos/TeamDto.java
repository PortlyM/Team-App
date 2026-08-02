package com.example.teamapp.teams.domain.dtos;

import com.example.teamapp.tasks.domain.dtos.TodoListDto;
import com.example.teamapp.user.domain.dtos.UserDto;
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
    private UserDto leader;
    private List<TodoListDto> todoLists = new ArrayList<>();
    private List<UserDto> members = new ArrayList<>();
}
