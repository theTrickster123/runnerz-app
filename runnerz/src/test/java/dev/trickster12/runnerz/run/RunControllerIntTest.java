package dev.trickster12.runnerz.run;

import jakarta.activation.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Ensures the test profile is loaded


@Transactional // Assure le rollback après chaque test
public class RunControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RunRepository runRepository;

    @MockBean // Mock the conflicting data source
    private DataSource jdbcConnectionDetailsForRunnerzAppMainPostgres1;


    @Test
    void shouldFetchAllRuns() throws Exception {
        // Préparer les données
        Run run1 = new Run("Morning Run", LocalDateTime.now().minusHours(2), LocalDateTime.now(), 5, Location.OUTDOOR, 1);
        Run run2 = new Run("Evening Run", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(22), 10, Location.INDOOR, 1);
        runRepository.saveAll(List.of(run1, run2));

        // Effectuer la requête et vérifier la réponse
        mockMvc.perform(get("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Morning Run")))
                .andExpect(jsonPath("$[1].title", is("Evening Run")));
    }

    @Test
    void shouldCreateNewRun() throws Exception {
        // JSON pour la création d'une nouvelle course
        String requestPayload = """
                {
                    "title": "Test Run",
                    "startedOn": "2023-10-01T10:00:00",
                    "completedOn": "2023-10-01T12:00:00",
                    "miles": 6,
                    "location": "INDOOR"
                }
                """;

        // Effectuer la requête et vérifier la réponse
        mockMvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isCreated());

        // Vérifier si la course a été enregistrée
        List<Run> runs = runRepository.findByTitle("Test Run");
        assert (!runs.isEmpty());
    }

    @Test
    void shouldFetchRunById() throws Exception {
        // Préparer une course dans la base
        Run run = new Run("Run by ID", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 8, Location.OUTDOOR, 1);
        Run savedRun = runRepository.save(run);

        // Effectuer la requête pour récupérer la course par ID
        mockMvc.perform(get("/api/runs/" + savedRun.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Run by ID")));
    }

    @Test
    void shouldUpdateRun() throws Exception {
        // Préparer une course existante
        Run run = new Run("Old Title", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 8, Location.INDOOR, 1);
        Run savedRun = runRepository.save(run);

        // JSON pour mettre à jour la course
        String requestPayload = """
                {
                    "title": "Updated Title",
                    "startedOn": "2023-10-01T10:00:00",
                    "completedOn": "2023-10-01T12:00:00",
                    "miles": 10,
                    "location": "OUTDOOR",
                    "version": 1
                }
                """;

        // Effectuer la requête PUT pour la mise à jour
        mockMvc.perform(put("/api/runs/" + savedRun.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isOk());

        // Vérifier si la mise à jour a été correctement effectuée
        Run updatedRun = runRepository.findById(savedRun.getId()).orElseThrow();
        assert ("Updated Title".equals(updatedRun.getTitle()));
        assert (updatedRun.getMiles() == 10);
    }

    @Test
    void shouldDeleteRun() throws Exception {
        // Préparer une course à supprimer
        Run run = new Run("Run to Delete", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 6, Location.OUTDOOR, 1);
        Run savedRun = runRepository.save(run);

        // Effectuer la requête DELETE
        mockMvc.perform(delete("/api/runs/" + savedRun.getId()))
                .andExpect(status().isNoContent());

        // Vérifier si la course a été supprimée
        assert (runRepository.findById(savedRun.getId()).isEmpty());
    }

    @Test
    void shouldThrowNotFoundWhenFetchingNonExistingRun() throws Exception {
        // Tenter de récupérer une course avec un ID qui n'existe pas
        mockMvc.perform(get("/api/runs/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByPattern() throws Exception {
        String requestPayload = """
                {
                    "title": "afffffs",
                    "startedOn": "2023-10-01T10:00:00",
                    "completedOn": "2023-10-01T12:00:00",
                    "miles": 6,
                    "location": "INDOOR"
                }
                """;

        // Effectuer la requête et vérifier la réponse
        mockMvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isCreated());
        // Existing match
        List<Run> runs = runRepository.findByPattern("a_____s");
        assertFalse(runs.isEmpty());
        assertEquals("afffffs", runs.get(0).getTitle());


    }

    @Test
    public void testFindByPatternTwo() throws Exception {
        String requestPayload = """
                {
                    "title": "sfffffa",
                    "startedOn": "2023-10-01T10:00:00",
                    "completedOn": "2023-10-01T12:00:00",
                    "miles": 6,
                    "location": "INDOOR"
                }
                """;

        // Effectuer la requête et vérifier la réponse
        mockMvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isCreated());
        // Existing match
        List<Run> runs = runRepository.findByPattern("s_____a");
        assertFalse(runs.isEmpty());
        assertEquals("sfffffa", runs.get(0).getTitle());


    }

}