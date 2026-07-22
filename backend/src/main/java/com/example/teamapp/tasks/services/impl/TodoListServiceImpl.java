package com.example.teamapp.tasks.services.impl;

import com.example.teamapp.tasks.domain.TodoListMapper;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.repository.TodoListRepository;
import com.example.teamapp.tasks.services.TodoListService;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {

    private final TodoListRepository todoListRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public TodoList createTodoList(UUID teamId, TodoList todoList) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + teamId + " does not exist"));
        todoList.setTeam(team);

        if (!todoListRepository.existsByName(todoList.getName())) {
            return todoListRepository.save(todoList);
        }
        throw new EntityExistsException("Todo list with name " + todoList.getName() + "already exists");
    }
}
