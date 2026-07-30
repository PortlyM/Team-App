package com.example.teamapp.services;

import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.teams.services.impl.TeamServiceImpl;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

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

    @Mock
    private UserRepository userRepository;

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

    @Test
    void addMember_ShouldReturnUpdatedListOfUser_WhenTeamAndUserExist() {
        User user = createTestUser();
        User user2 = createTestUser();
        Team testTeam = createTestTeam(user);
        List<User> listOfMembers = List.of(user, user2);

        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        List<User> result = teamService.addMember(testTeam.getId(), user2.getId());

        assertEquals(listOfMembers, result);

        verify(teamRepository, times(1)).findById(testTeam.getId());
        verify(userRepository, times(1)).findById(user2.getId());
    }

    @Test
    void addMember_ShouldThrowException_WhenTeamNotExists() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.addMember(teamId, userId)
        );

        assertEquals(exception.getMessage(), "Team with id " + teamId + " does not exist");
    }

    @Test
    void addMember_ShouldThrowException_WhenUserNotExists() {
        User user = createTestUser();
        Team testTeam = createTestTeam(user);
        UUID userId = UUID.randomUUID();

        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.addMember(testTeam.getId(), userId)
        );

        assertEquals(exception.getMessage(), "User with id " + userId + " does not exist");
    }

    @Test
    void deleteMember_ShouldReturnListOfUser_WhenTeamAndUsersExist() {
        User user = createTestUser();
        User user2 = createTestUser();
        Team testTeam = createTestTeam(user);
        testTeam.getMembers().add(user2);
        List<User> listOfMembers = List.of(user);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        List<User> result = teamService.deleteMember(testTeam.getId(), user2.getId(), user.getEmail());

        assertEquals(listOfMembers, result);

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(teamRepository, times(1)).findById(testTeam.getId());
        verify(userRepository, times(1)).findById(user2.getId());
    }

    @Test
    void deleteMember_ShouldThrowException_WhenNoUserExist() {
        String notExistingUserEmail = "test1@example.com";
        User user = createTestUser();
        Team testTeam = createTestTeam(user);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.deleteMember(testTeam.getId(), user.getId(), notExistingUserEmail)
        );

        assertEquals("User with email " + notExistingUserEmail + " not found", exception.getMessage());
    }

    @Test
    void deleteMember_ShouldThrowException_WhenTeamDoesNotExist() {
        User user = createTestUser();
        User user2 = createTestUser();
        UUID testTeamId = UUID.randomUUID();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.deleteMember(testTeamId, user2.getId(), user.getEmail())
        );

        assertEquals("Team with id " + testTeamId + " does not exist", exception.getMessage());

        verify(userRepository, never()).findById(user2.getId());
    }

    @Test
    void deleteMember_ShouldThrowException_WhenUserIsNotLeader() {
        User user = createTestUser();
        User user2 = createTestUser();
        Team testTeam = createTestTeam(user);
        testTeam.getMembers().add(user2);

        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));
        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> teamService.deleteMember(testTeam.getId(), user2.getId(), user2.getEmail())
        );

        assertEquals("You are not a leader of team " + testTeam.getName(), exception.getMessage());

        verify(userRepository, never()).findById(user2.getId());
    }
}
