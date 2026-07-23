package com.example.teamapp.tasks.services.impl;

import com.example.teamapp.tasks.domain.dtos.UpdateTaskStatus;
import com.example.teamapp.tasks.domain.enitities.Task;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TaskRepository;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.tasks.services.TaskService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;

    @Override
    @Transactional
    public Task createTask(Task task, UUID listId) {
        TodoList todoList = todoListRepository.findById(listId)
                    .orElseThrow(() -> new EntityNotFoundException("List with id " + listId + " not found"));
        task.setList(todoList);

        if (!taskRepository.existsByContent(task.getContent())) {
            return taskRepository.save(task);
        }
        throw new EntityExistsException("Task " + task.getContent() + " already exists");
    }

    @Override
    public Task updateTaskStatus(UUID taskId, UpdateTaskStatus updateTaskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        task.setStatus(updateTaskStatus.getStatus());
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task getTask(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
    }
}
