package ru.practicum.model.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @Min(value = 0, message = "Значение не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    @NotNull
    @NotBlank
    @NotEmpty
    private String stateAction;
}
