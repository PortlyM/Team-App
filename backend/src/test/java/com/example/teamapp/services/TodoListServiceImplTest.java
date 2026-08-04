package com.example.teamapp.services;

import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.tasks.services.impl.TodoListServiceImpl;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.user.domain.entity.User;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.teamapp.util.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceImplTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TodoListServiceImpl todoListService;

    @Test
    void createTodoList_ShouldReturnCreatedList_WhenNoListExist() {
        User testUser = createTestUser();
        Team testTeam = createTestTeam(testUser);
        TodoList todoList = createTestTodoList(testTeam);

        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));
        when(todoListRepository.existsByName(todoList.getName())).thenReturn(false);
        when(todoListRepository.save(todoList)).thenReturn(todoList);

        TodoList result = todoListService.createTodoList(testTeam.getId(), todoList);

        assertEquals(todoList, result);
        assertEquals(todoList.getTeam().getId(), testTeam.getId());

        verify(teamRepository, times(1)).findById(testTeam.getId());
        verify(todoListRepository, times(1)).existsByName(todoList.getName());
        verify(todoListRepository, times(1)).save(todoList);
    }

    @Test
    void createTodoList_ShouldReturnCreatedList_WhenListExists() {
        User testUser = createTestUser();
        Team testTeam = createTestTeam(testUser);
        TodoList todoList = createTestTodoList(testTeam);

        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));
        when(todoListRepository.existsByName(todoList.getName())).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> todoListService.createTodoList(testTeam.getId(), todoList)
        );

        assertEquals("Todo list with name " + todoList.getName() + " already exists", exception.getMessage());

        verify(teamRepository, times(1)).findById(testTeam.getId());
        verify(todoListRepository, times(1)).existsByName(todoList.getName());
        verify(todoListRepository, never()).save(todoList);
    }

    @Test
    void getAllLists_ShouldReturnListofLists() {
        User testUser = createTestUser();
        Team testTeam = createTestTeam(testUser);
        TodoList todoList = createTestTodoList(testTeam);
        TodoList todoList2 = createTestTodoList(testTeam);
        List<TodoList> listOfLists = List.of(todoList, todoList2);

        when(todoListRepository.findAllByTeam_Id(testTeam.getId())).thenReturn(listOfLists);

        List<TodoList> result = todoListService.getAllLists(testTeam.getId());

        assertEquals(listOfLists, result);

        verify(todoListRepository, times(1)).findAllByTeam_Id(testTeam.getId());
    }
}
