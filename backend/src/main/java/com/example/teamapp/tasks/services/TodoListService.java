package com.example.teamapp.tasks.services;

import com.example.teamapp.tasks.domain.enitities.TodoList;

import java.util.List;
import java.util.UUID;

public interface TodoListService {
    TodoList createTodoList(UUID teamId, TodoList todoList);
    void deleteTodoListById(UUID listId);
    List<TodoList> getAllLists(UUID teamId);
}
