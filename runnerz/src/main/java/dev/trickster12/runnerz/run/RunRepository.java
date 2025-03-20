package dev.trickster12.runnerz.run;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Integer> {


    List<Run> findByMiles(Integer miles);


    List<Run> findByLocation(Location location);


    List<Run> findByTitle(String title);


    List<Run> findByTitleContaining(String keyword);

    @Query("SELECT r FROM Run r WHERE r.title LIKE %:pattern%")
    List<Run> findByPattern(@Param("pattern") String pattern);


}
