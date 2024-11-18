package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.request.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {

    @Query(nativeQuery = true, value = "select count(*) " +
            "                             from event_requests er " +
            "                            where status = 'APPROVED' " +
            "                              and er.event_id = ?1 ")
    Long getConfirmedRequests(Long eventId);
}
