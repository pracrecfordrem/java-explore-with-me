package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.util.List;

public interface StatsRepository extends JpaRepository<Stats,Long> {
    @Query(nativeQuery = true, value =
                    "select app,\n" +
                    "       uri,\n" +
                    "       count(*) as hits\n" +
                    " from stats \n" +
                    "where timestamp <= ?1 " +
                    "  and timestamp >= ?2 " +
                    "  and " +
                    " group by app,\n" +
                    "      \t  uri ")
    List<Hit> getHits()
}
