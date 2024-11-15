package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventMapper;
import ru.practicum.model.event.NewEventDto;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestDto;
import ru.practicum.model.request.RequestMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.LocationRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class PublicController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final LocationRepository locationRepository;
    private final RequestService requestService;

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

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<Event> postEvent(
                                              @RequestBody NewEventDto newEventDto,
                                              @PathVariable Long userId
                                             ) {
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User user = userService.getUserById(userId);
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        if (category == null || user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventService.createEvent(EventMapper.toEvent(newEventDto,
                                                            category,
                                                            user,
                                                            location)),
                                         HttpStatus.CREATED);
        }
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<RequestDto> postRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (event == null || user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            Request request = new Request(null,user,event, event.getRequestModeration() ? "PENDING":"APPROVED",LocalDateTime.now());
            return new ResponseEntity<>(RequestMapper.toRequestDto(requestService.createRequest(request)),HttpStatus.CREATED);
        }
    }

}
