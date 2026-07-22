package com.example.teamapp.teams.domain;

import com.example.teamapp.teams.domain.dtos.CreateTeamRequest;
import com.example.teamapp.teams.domain.dtos.TeamDto;
import com.example.teamapp.teams.domain.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {
    Team toEntity(CreateTeamRequest createTeamRequest);
    TeamDto toDto(Team team);
    List<TeamDto> toDtoList(List<Team> teamsList);
}
