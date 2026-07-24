package com.example.teamapp.teams.services;

import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.user.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    Team saveTeam(Team team);
    List<Team> getAllTeams();
    Team getTeamById(UUID teamId);
    void deleteTeamById(UUID teamId);

    List<User> addMember(UUID teamId, UUID memberId);
    List<User> deleteMember(UUID teamId, UUID memberId, String name);
}
