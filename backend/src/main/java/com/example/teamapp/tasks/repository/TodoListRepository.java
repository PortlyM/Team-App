package com.example.teamapp.tasks.repository;

import com.example.teamapp.tasks.domain.enitities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoListRepository extends JpaRepository<TodoList, UUID> {
    boolean existsByName(String name);
}
