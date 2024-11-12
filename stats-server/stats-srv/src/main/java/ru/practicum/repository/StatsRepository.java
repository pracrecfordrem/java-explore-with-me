package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats,Long> {

    @Query(value =
                    "select new ru.practicum.model.Hit (s.app,\n" +
                    "       s.uri,\n" +
                    "       count(s.ip))\n" +
                    " from ru.practicum.model.Stats s \n" +
                    "where s.timestamp >= :start " +
                    "  and s.timestamp <= :end " +
                    "  and (:values IS NULL or s.uri in :values)" +
                    " group by s.app,\n" +
                    "      \t  s.uri ")
    List<Hit> getHits(@Param("start")LocalDateTime start, @Param("end")LocalDateTime end, @Param("values")List<String> values);

    @Query(value =
            "select new ru.practicum.model.Hit (s.app,\n" +
                    "       s.uri,\n" +
                    "       count(distinct s.ip))\n" +
                    " from ru.practicum.model.Stats s \n" +
                    "where s.timestamp >= :start " +
                    "  and s.timestamp <= :end " +
                    "  and (:values IS NULL or s.uri in :values)" +
                    " group by s.app,\n" +
                    "      \t  s.uri ")
    List<Hit> getDistinctIpHits(@Param("start")LocalDateTime start, @Param("end")LocalDateTime end, @Param("values")List<String> values);
}
