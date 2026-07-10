package com.example.teamapp.tasks.domain.enitities;

import com.example.teamapp.tasks.domain.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "todo_list_id", nullable = false)
    private TodoList list;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(content, task.content) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, status);
    }
}
