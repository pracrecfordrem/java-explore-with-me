package ru.practicum.model.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import ru.practicum.model.location.LocationDto;
@Data
public class NewEventDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String annotation;
    private Long category;
    @NotNull
    @NotBlank
    @NotEmpty
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @Min(value = 0, message = "Значение не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
