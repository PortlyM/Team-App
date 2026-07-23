package com.example.teamapp.tasks.domain;

import com.example.teamapp.tasks.domain.dtos.CreateTodoListRequest;
import com.example.teamapp.tasks.domain.dtos.TodoListDto;
import com.example.teamapp.tasks.domain.enitities.TodoList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoListMapper {

    TodoList toEntity(CreateTodoListRequest createTodoListRequest);
    TodoListDto toDto(TodoList todoList);
    List<TodoListDto> toDtoList(List<TodoList> entityList);
}
