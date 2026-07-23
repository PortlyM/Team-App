package com.example.teamapp.tasks.domain.dtos;

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
public class TodoListDto {

    private UUID id;
    private String name;
    private UUID teamId;
    private List<TaskDto> tasks = new ArrayList<>();
}
