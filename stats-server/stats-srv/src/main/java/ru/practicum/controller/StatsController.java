package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsRequestDto;
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
    public ResponseEntity<Object> post(@RequestBody StatsRequestDto statsRequestDto) {
        return new ResponseEntity<>(statsService.post(statsRequestDto),HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                        HttpServletRequest request
                           ) {
        if (LocalDateTime.parse(start,CUSTOM_FORMATTER).isAfter(LocalDateTime.parse(end,CUSTOM_FORMATTER))) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(statsService.get(
                LocalDateTime.parse(start,CUSTOM_FORMATTER),
                LocalDateTime.parse(end,CUSTOM_FORMATTER),uris,unique), HttpStatus.OK);
    }
}
