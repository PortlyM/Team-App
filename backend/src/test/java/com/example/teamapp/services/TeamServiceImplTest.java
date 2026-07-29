package com.example.teamapp.services;

import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.teams.services.impl.TeamServiceImpl;
import com.example.teamapp.user.domain.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.teamapp.util.TestDataUtil.createTestTeam;
import static com.example.teamapp.util.TestDataUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void saveTeam_ShouldReturnSavedTeam_WhenNoTeamExist() {
        User user = createTestUser();
        Team testTeam = createTestTeam(user);

        when(teamRepository.existsByName(testTeam.getName())).thenReturn(false);
        when(teamRepository.save(testTeam)).thenReturn(testTeam);

        Team result = teamService.saveTeam(testTeam);

        assertEquals(result, testTeam);

        verify(teamRepository, times(1)).existsByName(testTeam.getName());
        verify(teamRepository, times(1)).save(testTeam);
    }

    @Test
    void saveTeam_ShouldThrowException_WhenTeamWithNameExists() {
        User user = createTestUser();
        Team testTeam = createTestTeam(user);

        when(teamRepository.existsByName(testTeam.getName())).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> teamService.saveTeam(testTeam)
        );

        assertEquals(exception.getMessage(), "This team already exists");
        verify(teamRepository, never()).save(testTeam);
    }

    @Test
    void getAllTeams_ShouldReturnListOfTeams() {
        User user = createTestUser();
        Team testTeam = createTestTeam(user);
        Team testTeam2 = createTestTeam(user);
        List<Team> listOfTeams = List.of(testTeam, testTeam2);

        when(teamRepository.findAll()).thenReturn(listOfTeams);

        List<Team> result = teamService.getAllTeams();

        assertEquals(result, listOfTeams);

        verify(teamRepository, times(1)).findAll();
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() {
        User user = createTestUser();
        Team testTeam = createTestTeam(user);

        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));

        Team result = teamService.getTeamById(testTeam.getId());

        assertEquals(result, testTeam);

        verify(teamRepository, times(1)).findById(testTeam.getId());
    }

    @Test
    void getTeamById_ShouldThrowException_WhenNoTeamExists() {
        UUID randomUUID = UUID.randomUUID();

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.getTeamById(randomUUID)
        );

        assertEquals(exception.getMessage(), "Team not found " + randomUUID);
    }
}
