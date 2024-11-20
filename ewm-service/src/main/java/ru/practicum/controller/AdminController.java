package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventDto;
import ru.practicum.model.event.EventForUpdate;
import ru.practicum.model.event.EventMapper;
import ru.practicum.model.user.NewUserDto;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;
import ru.practicum.service.CategoryService;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMappper;
import ru.practicum.model.category.NewCategoryDto;
import ru.practicum.service.EventService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminController {

    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam List<Long> ids,
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(userService.getUsers(ids, from, size),HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEvents(@RequestParam(required = false) List<Long> users,
                                                    @RequestParam(required = false) List<String> states,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(required = false) Long from,
                                                    @RequestParam(required = false) Long size,
                                                    HttpServletRequest request) {
              return new ResponseEntity<>(
                eventService.getEvents(users,
                states,
                categories,
                LocalDateTime.parse(rangeStart,CUSTOM_FORMATTER),
                LocalDateTime.parse(rangeEnd,CUSTOM_FORMATTER),
                from,
                size).stream().
                        map(eventMapper::toEventDto).
                        toList(),HttpStatus.OK);

    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(CategoryMappper.toCategory(newCategoryDto)), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody NewUserDto newUserDto) {
        return new ResponseEntity<>(userService.createUser(UserMapper.toUser(newUserDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Category> updateCategory(@RequestBody NewCategoryDto newCategoryDto, @PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            category.setName(newCategoryDto.getName());
            return new ResponseEntity<>(categoryService.createCategory(category),HttpStatus.OK);
        }
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Event> updateEvent(@RequestBody EventForUpdate eventForUpdate,
                                             @PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else if (event.getState() != null && event.getState().equals("PUBLISHED")) {
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        } else {
            if (eventForUpdate.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState("PUBLISHED");
            }
            Category category = categoryService.getCategoryById(eventForUpdate.getCategory());
            if (eventForUpdate.getAnnotation() != null) {
                event.setAnnotation(eventForUpdate.getAnnotation());
            }
            if (eventForUpdate.getCategory() != null) {
                event.setCategory(category);
            }
            if (eventForUpdate.getDescription() != null) {
                event.setDescription(eventForUpdate.getDescription());
            }
            if (eventForUpdate.getTitle() != null) {
                event.setTitle(eventForUpdate.getTitle());
            }
            if (eventForUpdate.getParticipantLimit() != null) {
                event.setParticipantLimit(eventForUpdate.getParticipantLimit());
            }
            if (eventForUpdate.getEventDate() != null) {
                event.setEventDate(LocalDateTime.parse(eventForUpdate.getEventDate(),CUSTOM_FORMATTER));
            }
            return new ResponseEntity<>(eventService.createEvent(event),HttpStatus.OK);
        }
    }
}
























