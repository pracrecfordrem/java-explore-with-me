package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatClient;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventDto;
import ru.practicum.model.event.EventMapper;
import ru.practicum.model.exception.Exception;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class PublicController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String APP_NAME = "ExploreWithMe-main-service";

    @GetMapping("/categories")
    public List<Category> getCategories(@RequestParam(defaultValue = "0") Long from, @RequestParam(defaultValue = "10") Long size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Category> getCategories(@PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
    }


    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getPublicEvents(@Pattern(regexp = "^[^0-9]*$", message = "Строка не должна содержать цифры") @RequestParam(required = false) String text,
                                                                @RequestParam(required = false) List<Long> categories,
                                                                @RequestParam(required = false) Boolean paid,
                                                                @RequestParam(required = false) String rangeStart,
                                                                @RequestParam(required = false) String rangeEnd,
                                                                @RequestParam(required = false) Boolean onlyAvailable,
                                                                @RequestParam(required = false) String sort,
                                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                HttpServletRequest request) {
        statClient.post(APP_NAME,request);
        LocalDateTime rangeStartDt = rangeStart != null ? LocalDateTime.parse(rangeStart,CUSTOM_FORMATTER) : null;
        LocalDateTime rangeEndDt = rangeStart != null ? LocalDateTime.parse(rangeEnd,CUSTOM_FORMATTER) : null;
        List<EventDto> eventDtoList = eventService.getPublicEvents(text,
                        categories,
                        paid,
                        rangeStartDt,
                        rangeEndDt,
                        onlyAvailable,
                        sort).stream().
                map(event -> eventMapper.toEventDto(event,statClient)).toList();
        if (from > eventDtoList.size()) {
            eventDtoList = new ArrayList<>();
        } else if (size > eventDtoList.size()) {
            eventDtoList = eventDtoList.subList(from,eventDtoList.size());
        } else {
            eventDtoList = eventDtoList.subList(from,size);
        }
        return new ResponseEntity<>(eventDtoList,HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEvent(@PathVariable Long eventId, HttpServletRequest request) {
        statClient.post(APP_NAME,request);
        Event event = eventService.getEventById(eventId);
        if (event == null || !event.getState().equals("PUBLISHED")) {
            return new ResponseEntity<>(new Exception("NOT_FOUND",
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found",
                    LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventMapper.toEventDto(event,statClient),HttpStatus.OK);
        }
    }

    @GetMapping("/compilations")
    public ResponseEntity<List<Compilation>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Compilation> compilations = compilationService.getCompilations(pinned,from,size);
        if (from > compilations.size()) {
            compilations = new ArrayList<>();
        } else if (size > compilations.size()) {
            compilations = compilations.subList(from,compilations.size());
        } else {
            compilations = compilations.subList(from,size);
        }
        return new ResponseEntity<>(compilations,HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Compilation> getCompilation(@PathVariable Long compId) {
        Compilation compilation = compilationService.getCompilationById(compId);
        if (compilation == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(compilation,HttpStatus.OK);
    }

}
