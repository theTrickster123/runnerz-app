package dev.trickster12.runnerz.run;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc

// Complete the test class
public class RunControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private RunRepository runRepository; // Mocked repository

    private final List<Run> runs = new ArrayList<>();
    @BeforeEach
    void setUp() {
        runs.add(new Run(
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR,0));
        runs.add(new Run(
                "Wednesday Morning Run",
                LocalDateTime.now().plus(10, ChronoUnit.MINUTES),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR,0));
    }

    @Test
    void shouldFindAllRuns() throws Exception {
        when(runRepository.findAll()).thenReturn(runs);

        mvc.perform(MockMvcRequestBuilders.get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldFindOneRun() throws Exception {
        Run run = runs.get(0);
        when(runRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(run));
        mvc.perform(MockMvcRequestBuilders.get("/api/runs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(run.getId())))
                .andExpect(jsonPath("$.title", is(run.getTitle())))
                .andExpect(jsonPath("$.miles", is(run.getMiles())))
                .andExpect(jsonPath("$.location", is(run.getLocation().toString())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/runs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRun() throws Exception { //error
        var run = new Run("test", LocalDateTime.now(),LocalDateTime.now().plus(30,ChronoUnit.HOURS),1, Location.INDOOR,0);
        mvc.perform(MockMvcRequestBuilders.post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRun() throws Exception {
        // Create a mock existing Run entity
        Run existingRun = new Run();
        existingRun.setId(1);
        existingRun.setTitle("Old Run");
        existingRun.setStartedOn(LocalDateTime.parse("2020-01-01T06:00:00"));
        existingRun.setCompletedOn(LocalDateTime.parse("2020-01-01T07:00:00"));
        existingRun.setMiles(5);
        existingRun.setLocation(Location.INDOOR);
        existingRun.setVersion(0);  // Initial version is 0 for a new Run

        // Create the updated Run object
        Run updatedRun = new Run();
        updatedRun.setId(1);
        updatedRun.setTitle("Morning Run");
        updatedRun.setStartedOn(LocalDateTime.parse("2020-01-01T06:00:00"));
        updatedRun.setCompletedOn(LocalDateTime.parse("2020-01-01T07:00:00"));
        updatedRun.setMiles(6);
        updatedRun.setLocation(Location.INDOOR);
        updatedRun.setVersion(1);  // Version will be updated

        // Mock repository methods
        Mockito.when(runRepository.findById(1)).thenReturn(Optional.of(existingRun));  // Simulate finding the existing Run
        Mockito.when(runRepository.save(Mockito.any(Run.class))).thenReturn(updatedRun);  // Simulate saving the updated Run

        // Perform PUT request with the updated details
        mvc.perform(MockMvcRequestBuilders.put("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"title\": \"Morning Run\",\n" +
                                "  \"startedOn\": \"2020-01-01T06:00:00\",\n" +
                                "  \"completedOn\": \"2020-01-01T07:00:00\",\n" +
                                "  \"miles\": 6,\n" +
                                "  \"location\": \"INDOOR\",\n" +
                                "  \"version\": 1\n" +
                                "}"))
                .andExpect(status().isOk());  // Expect 200 OK status

        // Verify that save was called exactly once with any Run object
        Mockito.verify(runRepository, Mockito.times(1)).save(Mockito.any(Run.class));
    }




    @Test
    public void shouldDeleteRun() throws Exception {  //error resolved
        // Mock repository behavior: return a fake Run when findById() is called which will be put in an Optional
        Run run = new Run();
        run.setId(1);
        run.setTitle("Test Run");

        Mockito.when(runRepository.findById(1)).thenReturn(Optional.of(run));  // Simulate found Run : When runRepository.findById(1) is called, return an Optional containing the fake Run
        Mockito.doNothing().when(runRepository).deleteById(1);  // Simulate successful delete : When runRepository.deleteById(1) is called, do nothing to get the no content status

        // Perform DELETE request
        mvc.perform(MockMvcRequestBuilders.delete("/api/runs/1"))
                .andExpect(status().isNoContent());

        // Verify that deleteById() was actually called
        Mockito.verify(runRepository, Mockito.times(1)).deleteById(1); // Verify that runRepository.deleteById(1) was called exactly once
    }

    /*
        Here's what happens in the mocked execution:
            runRepository.findById(1) is called → Mockito returns Optional.of(run).
            Since run exists, runRepository.deleteById(1) is called → Mockito does nothing.
            The controller returns HTTP 204 No Content.
            The test checks if the status is 204, so it passes.
   */

    @Test
    void shouldReturnRunsWhenKeywordExists() throws Exception{
        Run run = new Run("Test Run", LocalDateTime.now(), LocalDateTime.now().plus(30, ChronoUnit.HOURS), 1, Location.INDOOR, 0);
        run.setId(1);  // Manually setting the ID for testing

        List<Run> mockRuns = List.of(run);

        Mockito.when(runRepository.findByTitleContaining("Test")).thenReturn(mockRuns);

        mvc.perform(MockMvcRequestBuilders.get("/api/runs/partial-title/Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Run"));

        Mockito.verify(runRepository).findByTitleContaining("Test");

    }



}


