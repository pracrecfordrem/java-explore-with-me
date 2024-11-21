package ru.practicum.model.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String annotation;
    private Category category;
    private String description;
    private String eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
    private Long confirmedRequests;
}
