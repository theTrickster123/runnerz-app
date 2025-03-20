package dev.trickster12.runnerz.run;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        var run = new Run("test", LocalDateTime.now(),LocalDateTime.now(),1, Location.INDOOR,0);
        mvc.perform(MockMvcRequestBuilders.post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRun() throws Exception { //error
        var run = new Run("test", LocalDateTime.now(),LocalDateTime.now(),1, Location.INDOOR,0);
        mvc.perform(MockMvcRequestBuilders.put("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteRun() throws Exception {  //error
        mvc.perform(MockMvcRequestBuilders.delete("/api/runs/1"))
                .andExpect(status().isNoContent());
    }
}