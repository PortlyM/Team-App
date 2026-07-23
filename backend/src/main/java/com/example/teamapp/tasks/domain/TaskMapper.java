package com.example.teamapp.tasks.domain;

import com.example.teamapp.tasks.domain.dtos.CreateTaskRequest;
import com.example.teamapp.tasks.domain.dtos.TaskDto;
import com.example.teamapp.tasks.domain.enitities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toEntity(CreateTaskRequest createTaskRequest);

    @Mapping(source = "list.id", target = "todoListId")
    TaskDto toDto(Task task);
}
