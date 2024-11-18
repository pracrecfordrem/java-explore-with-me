package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Event getEventById(Long eventId);

    @Query(value = " select e" +
            "          from ru.practicum.model.event.Event e " +
            "          join e.category c " +
            "          join e.initiator u " +
            "         where (:userIds IS NULL or u.id in :userIds) " +
            "           and (:states IS NULL or e.state in :states) " +
            "           and (:categories IS NULL or c.id in :categories)" +
            "           and (coalesce(:rangeStart,to_date('1900101','yyyymmdd')) = to_date('1900101','yyyymmdd') or e.eventDate >= :rangeStart)" +
            "           and (coalesce(:rangeEnd,to_date('1900101','yyyymmdd')) = to_date('1900101','yyyymmdd') or e.eventDate <= :rangeEnd)" +
            "           and ()")
    List<Event> getEvents(@Param("userIds") List<Long> userIds,
                          @Param("states") List<String> states,
                          @Param("categories") List<Long> categories,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd);


    @Query(nativeQuery = true, value = "select count(*) " +
            "                             from event_views ev " +
            "                            where ev.event_id = ?1 " )
    Long getViews(Long eventId);
}
