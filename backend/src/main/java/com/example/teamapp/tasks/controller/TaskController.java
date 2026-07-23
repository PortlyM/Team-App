package com.example.teamapp.tasks.controller;

import com.example.teamapp.tasks.domain.TaskMapper;
import com.example.teamapp.tasks.domain.dtos.CreateTaskRequest;
import com.example.teamapp.tasks.domain.dtos.TaskDto;
import com.example.teamapp.tasks.domain.dtos.UpdateTaskStatus;
import com.example.teamapp.tasks.domain.enitities.Task;
import com.example.teamapp.tasks.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/todolists/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        UUID listId = createTaskRequest.getTodoListId();
        Task taskEntity = taskMapper.toEntity(createTaskRequest);
        Task createdTaskEntity = taskService.createTask(taskEntity, listId);
        TaskDto mappedCreatedTask = taskMapper.toDto(createdTaskEntity);
        return new ResponseEntity<>(mappedCreatedTask, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{taskid}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID taskid, @RequestBody UpdateTaskStatus updateTaskStatus) {
        Task updatedTask = taskService.updateTaskStatus(taskid, updateTaskStatus);
        TaskDto mappedUpdatedTask = taskMapper.toDto(updatedTask);
        return new ResponseEntity<>(mappedUpdatedTask, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{taskid}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskid) {
        taskService.deleteTask(taskid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{taskid}")
    public ResponseEntity<TaskDto> getTask(@PathVariable UUID taskid) {
        Task task = taskService.getTask(taskid);
        TaskDto mappedTask = taskMapper.toDto(task);
        return new ResponseEntity<>(mappedTask, HttpStatus.OK);
    }

}
