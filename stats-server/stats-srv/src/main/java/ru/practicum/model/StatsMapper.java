package ru.practicum.model;
import ru.practicum.StatsRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Stats toStats(StatsRequestDto statsRequestDto) {
        return new Stats(
                statsRequestDto.getApp(),
                statsRequestDto.getUri(),
                statsRequestDto.getIp(),
                LocalDateTime.parse(statsRequestDto.getTimestamp(), CUSTOM_FORMATTER)
        );
    }
}
