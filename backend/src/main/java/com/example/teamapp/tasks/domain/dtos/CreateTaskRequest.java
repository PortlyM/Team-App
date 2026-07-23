package com.example.teamapp.tasks.domain.dtos;

import com.example.teamapp.tasks.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {

    private String content;
    private TaskStatus status;
    private UUID todoListId;
}
