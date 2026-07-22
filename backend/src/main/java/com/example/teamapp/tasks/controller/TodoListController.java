package com.example.teamapp.tasks.controller;

import com.example.teamapp.tasks.domain.TodoListMapper;
import com.example.teamapp.tasks.domain.dtos.CreateTodoListRequest;
import com.example.teamapp.tasks.domain.dtos.TodoListDto;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import com.example.teamapp.tasks.services.TodoListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/todolists")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListMapper todoListMapper;
    private final TodoListService todoListService;

    @PostMapping(path = "/{teamid}")
    public ResponseEntity<TodoListDto> createTodoList(@PathVariable UUID teamid, @Valid @RequestBody CreateTodoListRequest todoListRequest) {
        TodoList todoListRequestEntity = todoListMapper.toEntity(todoListRequest);
        TodoList createdTodoList = todoListService.createTodoList(teamid, todoListRequestEntity);
        TodoListDto mappedCreatedTodoList = todoListMapper.toDto(createdTodoList);
        return new ResponseEntity<>(mappedCreatedTodoList, HttpStatus.CREATED);
    }


}
