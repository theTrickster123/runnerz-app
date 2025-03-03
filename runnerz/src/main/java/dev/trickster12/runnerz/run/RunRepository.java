package dev.trickster12.runnerz.run;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

@Repository
public class RunRepository {

    private Logger logger = LoggerFactory.getLogger(RunRepository.class);
    private JdbcClient jdbcClient;

    public RunRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll(){
        return jdbcClient.sql("SELECT * FROM RUN")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(@PathVariable Integer id){
        return jdbcClient.sql("SELECT * FROM RUN WHERE ID = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(@Valid @RequestBody Run run){
        // Vérifier la validité des champs
        if (run.getTitle() == null || run.getStartedOn() == null || run.getCompletedOn() == null || run.getMiles() == null || run.getLocation() == null) {
            throw new IllegalArgumentException("Certains champs sont manquants pour Run: " + run);
        }

        // Exécuter la requête SQL en utilisant des paramètres nommés
        var updated = jdbcClient.sql("INSERT INTO RUN(TITLE, STARTED_ON, COMPLETED_ON, MILES, LOCATION) " +
                        "VALUES(:title, :startedOn, :completedOn, :miles, :location)")
                .param("title", run.getTitle())
                .param("startedOn", run.getStartedOn())
                .param("completedOn", run.getCompletedOn())
                .param("miles", run.getMiles())
                .param("location", run.getLocation().name()) // Convertir l'énumération en chaîne
                .update();

        Assert.state(updated == 1, "Échec de la création du run: " + run.getTitle());
    }

    public void update(@Valid @RequestBody Run run, @PathVariable Integer id){
        // Vérification de la validité des champs
        if (run.getTitle() == null || run.getStartedOn() == null || run.getCompletedOn() == null || run.getMiles() == null || run.getLocation() == null) {
            throw new IllegalArgumentException("Certains champs sont manquants pour Run: " + run);
        }

        var updated = jdbcClient.sql("UPDATE RUN SET TITLE = :title, STARTED_ON = :startedOn, COMPLETED_ON = :completedOn, MILES = :miles, LOCATION = :location WHERE ID = :id")
                .param("title", run.getTitle())
                .param("startedOn", run.getStartedOn())
                .param("completedOn", run.getCompletedOn())
                .param("miles", run.getMiles())
                .param("location", run.getLocation().name()) // Assure-toi que Location est une énumération stockée sous forme de chaîne
                .param("id", id) // Paramètre ID correctement utilisé
                .update();
        Assert.state(updated == 1, "Échec de la mise à jour du run: " + run.getTitle());
    }

    public void delete(@PathVariable Integer id){
        var updated = jdbcClient.sql("DELETE FROM RUN WHERE ID = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Échec de la suppression du run avec ID: " + id);
    }

    public int count(){
        return jdbcClient.sql("SELECT * FROM RUN")
                .query()
                .listOfRows()
                .size();
    }

    public void saveAll(@Valid @RequestBody List<Run> runs){
        runs.forEach(this::create);
    }

    public List<Run> findByLocation(@PathVariable Location location){
        return jdbcClient.sql("SELECT * FROM RUN WHERE LOCATION = :location")
                .param("location", location.name()) // Convertir l'énumération en chaîne
                .query(Run.class)
                .list();
    }

    public List<Run> findByTitle(@PathVariable String title){
        return jdbcClient.sql("SELECT * FROM RUN WHERE TITLE = :title")
                .param("title", title)
                .query(Run.class)
                .list();
    }

    public List<Run> findByMiles(@PathVariable Integer miles){
        return jdbcClient.sql("SELECT * FROM RUN WHERE MILES = :miles")
                .param("miles", miles)
                .query(Run.class)
                .list();
    }

    public List<Run> findByPartialTitle(@PathVariable String keyword){
        return jdbcClient.sql("SELECT * FROM RUN WHERE TITLE LIKE :keyword")
                .param("keyword","%"+keyword+"%")
                .query(Run.class)
                .list();
    }

    public List<Run> findByPattern(){
        return jdbcClient.sql("SELECT * FROM RUN WHERE TITLE LIKE :pattern")
                .param("pattern","a_____s")
                .query(Run.class)
                .list();
    }

}