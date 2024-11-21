package ru.practicum.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.location.LocationDto;

import java.time.LocalDateTime;

import static ru.practicum.model.request.RequestDto.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventForUpdate {
    @Size(min = 20, max = 2000, message = "Имя должно содержать не менее 20 символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Имя должно содержать не менее 20 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Future
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @Min(value = 0, message = "Значение не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Имя должно содержать не менее 20 символов")
    private String title;
    private String stateAction;
}
