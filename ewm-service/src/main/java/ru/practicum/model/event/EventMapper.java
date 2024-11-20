package ru.practicum.model.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.model.category.Category;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.StatClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class EventMapper {

    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public static Event toEvent(NewEventDto newEventDto,
                                Category category,
                                User user,
                                Location location) {

        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), CUSTOM_FORMATTER),
                LocalDateTime.now(),
                null,
                user,
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration(),
                "PENDING",
                newEventDto.getTitle()
        );
    }

    public EventDto toEventDto(Event event, StatClient statClient) {
        Long confirmedRequests = requestRepository.getConfirmedRequests(event.getId());
        getViews(statClient,event.getId());
        return new EventDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate().format(CUSTOM_FORMATTER),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getInitiator(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L,
                confirmedRequests
        );
    }

    private static long getViews(StatClient statClient, Long eventId) {

        ResponseEntity<Object> response = statClient.get(LocalDateTime.of(1900,1,1,0,0,0).toString(),
                                                         LocalDateTime.of(4999,1,1,0,0,0).toString(),
                                                         List.of("/events/"+eventId),
                                                         true);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> statsResponseList = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });
        System.out.println(statsResponseList);
        return 0L;
    }
}
