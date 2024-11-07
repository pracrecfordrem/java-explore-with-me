package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsRequestDto;
import ru.practicum.model.Stats;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;
    @PostMapping("/hit")
    public Stats post(@RequestBody StatsRequestDto statsRequestDto) {
        return statsService.post(statsRequestDto);
    }
    @GetMapping("/stats")
    public List<Stats> get(@RequestParam String start,
                           @RequestParam String end,
                           @RequestParam(required = false) List<String> uris,
                           @RequestParam(required = false) Boolean isUnique
                           ) {
        return statsService.get(start,end,uris,isUnique);
    }
}
