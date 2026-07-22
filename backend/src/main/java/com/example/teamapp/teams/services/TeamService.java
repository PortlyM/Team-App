package com.example.teamapp.teams.services;

import com.example.teamapp.teams.domain.entity.Team;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    Team saveTeam(Team team);
    List<Team> getAllTeams();
    Team getTeamById(UUID teamId);
}
