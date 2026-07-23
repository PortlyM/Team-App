package com.example.teamapp.tasks.services;

import com.example.teamapp.tasks.domain.enitities.TodoList;

import java.util.UUID;

public interface TodoListService {
    TodoList createTodoList(UUID teamId, TodoList todoList);
    void deleteTodoListById(UUID listId);
}
