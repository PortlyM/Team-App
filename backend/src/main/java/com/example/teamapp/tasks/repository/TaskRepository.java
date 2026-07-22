package com.example.teamapp.tasks.repository;

import com.example.teamapp.tasks.domain.enitities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
