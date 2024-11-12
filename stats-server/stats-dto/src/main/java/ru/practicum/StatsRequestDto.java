package ru.practicum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class StatsRequestDto {
    @NotBlank(message = "App name shouldn't be null or empty")
    private String app;
    @NotBlank(message = "Uri shouldn't be null or empty")
    private String uri;
    @NotBlank(message = "Ip shouldn't be null or empty")
    private String ip;
    @NotBlank(message = "Timestamp shouldn't be null or empty")
    private String timestamp;

}
