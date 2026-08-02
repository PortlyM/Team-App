package com.example.teamapp.controllers;

import com.example.teamapp.teams.domain.dtos.CreateTeamRequest;
import com.example.teamapp.teams.domain.entity.Team;
import com.example.teamapp.teams.repository.TeamRepository;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static com.example.teamapp.util.TestDataUtil.createTestControllerTeam;
import static com.example.teamapp.util.TestDataUtil.createTestControllerUser;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeamControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void createTeam_ShouldReturnCreatedStatusAndDto() throws Exception{
        CreateTeamRequest createTeamRequest = CreateTeamRequest.builder().name("TestTeam").build();

        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTeamRequest))
        )
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.name", is("TestTeam")))
                .andExpect((ResultMatcher) jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    void getAllTeams_ShouldReturnTeamsList() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam1 = createTestControllerTeam("TestTeam1", testUser);
        Team testTeam2 = createTestControllerTeam("TestTeam2", testUser);
        teamRepository.save(testTeam1);
        teamRepository.save(testTeam2);

        mockMvc.perform(get("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].name", is(testTeam1.getName())))
                .andExpect((ResultMatcher) jsonPath("$[1].name", is(testTeam2.getName())));
    }

    @Test
    @WithMockUser
    void getTeamById_ShouldReturnOkStatusAndDto() throws Exception{
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);

        mockMvc.perform(get("/api/v1/teams/{teamid}", testTeam.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", is(testTeam.getName())))
                .andExpect((ResultMatcher) jsonPath("$.leader.name", is(testUser.getName())));
    }

    @Test
    @WithMockUser
    void deleteTeamById_ShouldReturnNoContent() throws Exception {
        User testUser = createTestControllerUser();
        userRepository.save(testUser);
        Team testTeam = createTestControllerTeam(testUser);
        teamRepository.save(testTeam);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/api/v1/teams/{teamid}", testTeam.getId()))
                .andExpect(status().isNoContent());

        boolean teamExistsAfterDelete = teamRepository.existsById(testTeam.getId());
        assertFalse(teamExistsAfterDelete, "Team has not been deleted");
    }
}
