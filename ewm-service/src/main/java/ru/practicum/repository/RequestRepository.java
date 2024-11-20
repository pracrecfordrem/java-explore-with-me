package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.request.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    @Query(nativeQuery = true, value = "select count(*) " +
            "                             from event_requests er " +
            "                            where status = 'CONFIRMED' " +
            "                              and er.event_id = ?1 ")
    Long getConfirmedRequests(Long eventId);

    @Query(nativeQuery = true, value = "select r.* " +
            "                             from event_requests r" +
            "                            where r.user_id = ?1")
    List<Request> getRequestsByRequester(Long requesterId);

    @Query(nativeQuery = true, value = "select r.* " +
            "                             from event_requests r" +
            "                            where r.event_id = ?1")
    List<Request> getPersonalEventRequests(Long eventId);

    @Query(nativeQuery = true, value = "select er.* " +
            "                             from event_requests er " +
            "                            where er.event_id = ?1 ")
    List<Request> getRequestsByEventId(Long eventId);
}
