package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationDto;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class EventMapper {
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
}
