package com.example.teamapp.teams.services.impl;

import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.teams.services.TeamService;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Team saveTeam(Team team) {
        if (!teamRepository.existsByName(team.getName())) {
            return teamRepository.save(team);
        }
        throw new EntityExistsException("This team already exists");
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found " + teamId));
    }

    @Override
    public void deleteTeamById(UUID teamId) {
        teamRepository.deleteById(teamId);
    }

    @Override
    @Transactional
    public List<User> addMember(UUID teamId, UUID memberId) {
        Team actualTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + teamId + "does not exist"));
        List<User> listOfMembers = actualTeam.getMembers();
        User addedUser = userRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("User with id " + memberId + "does not exist"));
        listOfMembers.add(addedUser);
        return listOfMembers;
    }

    @Override
    @Transactional
    public List<User> deleteMember(UUID teamId, UUID memberId) {
        Team actualTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + teamId + "does not exist"));
        List<User> listOfMembers = actualTeam.getMembers();
        User deletedUser = userRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + memberId + "does not exist"));
        listOfMembers.remove(deletedUser);
        return listOfMembers;
    }
}
