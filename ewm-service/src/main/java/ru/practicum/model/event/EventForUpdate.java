package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.location.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventForUpdate {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private String stateAction;
}
