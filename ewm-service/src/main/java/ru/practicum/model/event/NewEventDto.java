package ru.practicum.model.event;

import lombok.Data;
import ru.practicum.model.location.LocationDto;
@Data
public class NewEventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participationLimit;
    private Boolean requestModeration;
    private String title;
}
