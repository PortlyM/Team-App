package com.example.teamapp.teams.controller;

import com.example.teamapp.teams.domain.TeamMapper;
import com.example.teamapp.teams.domain.dtos.CreateTeamRequest;
import com.example.teamapp.teams.domain.dtos.TeamDto;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@Valid @RequestBody CreateTeamRequest createTeamRequest) {
        Team mappedTeamRequest = teamMapper.toEntity(createTeamRequest);
        Team savedTeam = teamService.saveTeam(mappedTeamRequest);
        TeamDto savedTeamDto = teamMapper.toDto(savedTeam);
        return new ResponseEntity<>(savedTeamDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        List<Team> listOfEntityTeams = teamService.getAllTeams();
        List<TeamDto> mappedEntityTeamsList = teamMapper.toDtoList(listOfEntityTeams);
        return new ResponseEntity<>(mappedEntityTeamsList, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable UUID id) {
        Team searchedTeam = teamService.getTeamById(id);
        TeamDto searchedTeamDto = teamMapper.toDto(searchedTeam);
        return new ResponseEntity<>(searchedTeamDto, HttpStatus.OK);
    }
}
