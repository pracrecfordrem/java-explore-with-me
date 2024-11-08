package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsRequestDto;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsService statsService;

    @PostMapping("/hit")
    public Stats post(@RequestBody StatsRequestDto statsRequestDto) {
        return statsService.post(statsRequestDto);
    }

    @GetMapping("/stats")
    public List<Hit> get(@RequestParam String start,
                         @RequestParam String end,
                         @RequestParam(required = false) List<String> uris,
                         @RequestParam(required = false, defaultValue = "false") Boolean isUnique
                           ) {
        System.out.println(uris);
        System.out.println(isUnique);
        System.out.println(LocalDateTime.parse(start,CUSTOM_FORMATTER) + " " + LocalDateTime.parse(end,CUSTOM_FORMATTER));
        return statsService.get(LocalDateTime.parse(start,CUSTOM_FORMATTER),LocalDateTime.parse(end,CUSTOM_FORMATTER),uris,isUnique);
    }
}
