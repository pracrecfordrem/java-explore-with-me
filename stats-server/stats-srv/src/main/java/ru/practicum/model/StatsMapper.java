package ru.practicum.model;
import ru.practicum.StatsRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static Stats toStats(StatsRequestDto statsRequestDto) {
        return new Stats(
                statsRequestDto.getApp(),
                statsRequestDto.getUri(),
                statsRequestDto.getIp(),
                LocalDateTime.parse(statsRequestDto.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
