package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.getEventById(eventId);
    }

    public List<Event> getEvents(List<Long> userIds, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size) {
        return eventRepository.getEvents(userIds,states,categories,rangeStart,rangeEnd,null);
    }

    public List<Event> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size) {
        return eventRepository.getEvents(null,null,categories,rangeStart,rangeEnd,text);
    }
}
