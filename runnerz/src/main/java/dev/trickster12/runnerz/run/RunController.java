package dev.trickster12.runnerz.run;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
// working with the run repository
@RestController
@RequestMapping("/api/runs")
public class RunController {


    private final RunRepository runRepository;

    @Autowired
    public RunController(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    // Request to fetch all runs

    @GetMapping("")
    public List<Run> findAll(){
        return runRepository.findAll();
    }

    // Request to fetch a run by id

    @GetMapping("/{id}")
    public Run findById(@PathVariable Integer id){

        Optional<Run> run = runRepository.findById(id);
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }

        return run.get();

    }

    // post create a run

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@Valid @RequestBody Run run){
        runRepository.save(run);
    }

    // put update a run

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Run run, @PathVariable Integer id) {
        Optional<Run> existingRun = runRepository.findById(id);
        if (existingRun.isEmpty()) {
            throw new RunNotFoundException();
        }

        Run currentRun = existingRun.get();  // Get the existing entity from the database

        // When I wanted to update a run the version was not incremented
        // so I had to set the version of the run in the request body to the version of the run in the database to avoid the optimistic locking exception
        // Problem solved with setting the version of the run in the request body to the version of the run in the database

        // Update only the fields that are passed in the request body
        currentRun.setTitle(run.getTitle());
        currentRun.setStartedOn(run.getStartedOn());
        currentRun.setCompletedOn(run.getCompletedOn());
        currentRun.setMiles(run.getMiles());
        currentRun.setLocation(run.getLocation());

        // Set the ID and version explicitly, if necessary
        currentRun.setId(id);  // Just to be sure, set the ID (though it should already be set)
        currentRun.setVersion(run.getVersion());  // Set the version to maintain consistency for optimistic locking

        // Save the updated entity
        runRepository.save(currentRun);  // Save the updated entity
    }

    // delete delete a run

//    @ResponseStatus(HttpStatus.OK)
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Integer id){
//        Optional<Run> existingRun = runRepository.findById(id);
//        if (existingRun.isEmpty()) {
//            throw new RunNotFoundException();
//        }
//        runRepository.delete(existingRun.get());
//    }
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @DeleteMapping("/{id}")
        public void delete(@PathVariable Integer id){
            Optional<Run> existingRun = runRepository.findById(id);
            if (existingRun.isEmpty()) {
                throw new RunNotFoundException();
            }
            runRepository.deleteById(id);
        }

    // count all runs
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    public long count(){
        return runRepository.count();
    }

    //create multiple runs

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/saveAll")
    public void saveAll(@RequestBody List<Run> runs){
        List<Run> finalRunList= runs;
        runRepository.saveAll(finalRunList);

    }

    // fetch runs by location

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("location/{location}")
    public List<Run> findByLocation(@PathVariable Location location){

        List<Run> run = runRepository.findByLocation(location);
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }
        return run;

    }

    // fetch runs by title

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("title/{title}")
    public List<Run> findByTitle(@PathVariable String title){
        List<Run> run = runRepository.findByTitle(title);
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }
        return run;
    }

    //fetch runs by miles

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("miles/{miles}")
    public List<Run> findByMiles(@PathVariable Integer miles){
        List<Run> run = runRepository.findByMiles(miles);
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }
        return run;
    }

    //fetch runs by partial Title

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("partial-title/{keyword}")
    public List<Run> findByPartialTitle(@PathVariable String keyword) {
        List<Run> runs = runRepository.findByTitleContaining(keyword);  // Appel direct au repository
        if (runs.isEmpty()) {
            throw new RunNotFoundException();  // Si aucune course n'est trouvée, on lance une exception
        }
        return runs;
    }

    //fetch runs using a precise pattern : start with a ends with s and have 5 random characters in the middle

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("specific-pattern/version1")
    public List<Run> findRunByPattern(){
            List<Run> run = runRepository.findByPattern("a_____s");
            if(run.isEmpty()){
                throw new RunNotFoundException();
            }
            return run;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("specific-pattern/version2")
    public List<Run> findRunByPatternSecondVersion(){
        List<Run> run = runRepository.findByPattern("s_____a");
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }
        return run;
    }




}
