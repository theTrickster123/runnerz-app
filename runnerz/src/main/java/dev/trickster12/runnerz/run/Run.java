package dev.trickster12.runnerz.run;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String title;

    private LocalDateTime startedOn;

    private LocalDateTime completedOn;

    @Positive
    private Integer miles;

    @Enumerated(EnumType.STRING)
    private Location location;

    public Run() {
        // Constructeur par défaut pour JPA
    }

    public Run(String title, LocalDateTime startedOn, LocalDateTime completedOn, Integer miles, Location location) {
        if (!startedOn.isBefore(completedOn)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        this.title = title;
        this.startedOn = startedOn;
        this.completedOn = completedOn;
        this.miles = miles;
        this.location = location;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartedOn() {
        return startedOn;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public Integer getMiles() {
        return miles;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartedOn(LocalDateTime startedOn) {
        this.startedOn = startedOn;
    }

    public void setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}




