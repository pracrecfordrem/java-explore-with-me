package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsRequestDto;
import ru.practicum.model.Stats;
import ru.practicum.service.StatsService;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;
    @PutMapping
    public Stats post(@RequestBody StatsRequestDto statsRequestDto) {
        return statsService.post(statsRequestDto);
    }
}
