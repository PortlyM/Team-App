package com.example.teamapp.teams.controller;

import com.example.teamapp.teams.domain.TeamMapper;
import com.example.teamapp.teams.domain.dtos.CreateTeamRequest;
import com.example.teamapp.teams.domain.dtos.TeamDto;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.services.TeamService;
import com.example.teamapp.user.domain.UserMapper;
import com.example.teamapp.user.domain.dtos.UserDto;
import com.example.teamapp.user.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final UserMapper userMapper;

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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable UUID id) {
        teamService.deleteTeamById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{teamid}/members/{memberid}")
    public ResponseEntity<List<UserDto>> addMember(@PathVariable UUID teamid, @PathVariable UUID memberid) {
        List<User> updatedMemberList = teamService.addMember(teamid, memberid);
        List<UserDto> mappedUpdatedMemberList = userMapper.toDtoList(updatedMemberList);
        return new ResponseEntity<>(mappedUpdatedMemberList, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{teamid}/members/{memberid}")
    public ResponseEntity<List<UserDto>> deleteMember(@PathVariable UUID teamid, @PathVariable UUID memberid, Principal principal) {
        List<User> updatedMemberList = teamService.deleteMember(teamid, memberid, principal.getName());
        List<UserDto> mappedUpdatedMemberList = userMapper.toDtoList(updatedMemberList);
        return new ResponseEntity<>(mappedUpdatedMemberList, HttpStatus.OK);
    }
}
