package com.example.teamapp.tasks.domain.enitities;

import com.example.teamapp.teams.domain.entity.Team;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "todolists")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Builder.Default
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TodoList todoList = (TodoList) o;
        return Objects.equals(id, todoList.id) && Objects.equals(name, todoList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
