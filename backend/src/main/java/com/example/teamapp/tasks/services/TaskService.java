package com.example.teamapp.tasks.services;

import com.example.teamapp.tasks.domain.dtos.UpdateTaskStatus;
import com.example.teamapp.tasks.domain.enitities.Task;

import java.util.UUID;

public interface TaskService {
    Task createTask(Task task, UUID listId);
    Task updateTaskStatus(UUID taskId, UpdateTaskStatus updateTaskStatus);
}
