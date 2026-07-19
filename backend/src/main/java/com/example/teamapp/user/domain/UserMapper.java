package com.example.teamapp.user.domain;

import com.example.teamapp.user.domain.dtos.RegisterUserRequest;
import com.example.teamapp.user.domain.dtos.UserDto;
import com.example.teamapp.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterUserRequest registerUserRequest);

    UserDto toDto(User user);
}
