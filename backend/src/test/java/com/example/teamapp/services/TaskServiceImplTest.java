package com.example.teamapp.services;

import com.example.teamapp.tasks.domain.TaskStatus;
import com.example.teamapp.tasks.domain.dtos.UpdateTaskStatus;
import com.example.teamapp.tasks.domain.enitities.Task;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TaskRepository;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.tasks.services.impl.TaskServiceImpl;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.user.domain.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.example.teamapp.util.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TodoListRepository todoListRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_ShouldReturnTask_WhenNoTaskExist() {
        User testUser = createTestUser();
        Team testTeam = createTestTeam(testUser);
        TodoList todoList = createTestTodoList(testTeam);
        Task task = createTestTask(todoList);

        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));
        when(taskRepository.existsByContent(task.getContent())).thenReturn(false);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task, todoList.getId());

        assertEquals(task, result);

        verify(todoListRepository, times(1)).findById(todoList.getId());
        verify(taskRepository, times(1)).existsByContent(task.getContent());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void createTask_ShouldThrowException_WhenTaskExists() {
        User testUser = createTestUser();
        Team testTeam = createTestTeam(testUser);
        TodoList todoList = createTestTodoList(testTeam);
        Task task = createTestTask(todoList);

        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));
        when(taskRepository.existsByContent(task.getContent())).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> taskService.createTask(task, todoList.getId())
        );

        assertEquals("Task " + task.getContent() + " already exists", exception.getMessage());

        verify(taskRepository, never()).save(task);
    }

    @Test
    void createTask_ShouldThrowException_WhenListNotExist() {
        UUID listId = UUID.randomUUID();
        Task task = createTestTask(null);

        when(todoListRepository.findById(listId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.createTask(task, listId)
        );

        assertEquals("List with id " + listId + " not found", exception.getMessage());

        verify(taskRepository, never()).existsByContent(task.getContent());
    }

    @Test
    void updateTask_ShouldReturnTask() {
        Task task = createTestTask(null);
        UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus(TaskStatus.COMPLETED);
        task.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.updateTaskStatus(task.getId(), updateTaskStatus);

        assertEquals(task.getStatus(), result.getStatus());

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotExist() {
        UUID fakeTaskId = UUID.randomUUID();
        UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(fakeTaskId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.updateTaskStatus(fakeTaskId, updateTaskStatus)
        );

        assertEquals("Task with id " + fakeTaskId + " not found", exception.getMessage());
    }

    @Test
    void getTask_ShouldReturnTask() {
        Task task = createTestTask(null);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task result = taskService.getTask(task.getId());

        assertEquals(task, result);

        verify(taskRepository, times(1)).findById(task.getId());
    }

    @Test
    void getTask_ShouldThrowException_WhenTaskNotExist() {
        UUID fakeTaskId = UUID.randomUUID();

        when(taskRepository.findById(fakeTaskId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.getTask(fakeTaskId)
        );

        assertEquals("Task with id " + fakeTaskId + " not found", exception.getMessage());
    }
}
