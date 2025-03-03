package dev.trickster12.runnerz.run;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

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
        runRepository.create(run);
    }

    // put update a run

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Run run, @PathVariable Integer id){
        Optional<Run> existingRun = runRepository.findById(id);
        if (existingRun.isEmpty()) {
            throw new RunNotFoundException();
        }
        runRepository.update(run, id);
    }

    // delete delete a run

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        Optional<Run> existingRun = runRepository.findById(id);
        if (existingRun.isEmpty()) {
            throw new RunNotFoundException();
        }
        runRepository.delete(id);
    }

    // count all runs
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    public int count(){
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
    public List<Run> findByPartialTitle(@PathVariable String keyword){
        List<Run> run = runRepository.findByPartialTitle(keyword);
        if(run.isEmpty()){
            throw new RunNotFoundException();
        }
        return run;
    }

    //fetch runs using a precise pattern : start with a ends with s and have 5 random characters in the middle

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("specific-pattern")
    public List<Run> findByPattern(){
            List<Run> run = runRepository.findByPattern();
            if(run.isEmpty()){
                throw new RunNotFoundException();
            }
            return run;
    }



}
