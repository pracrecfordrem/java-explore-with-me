package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Event;

public interface EventRepository extends JpaRepository<Event,Long> {
}
