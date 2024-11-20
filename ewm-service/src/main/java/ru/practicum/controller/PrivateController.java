package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.*;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.request.*;
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
public class PrivateController {
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationRepository locationRepository;
    private final RequestService requestService;

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventDto>> getUserAddedEvents(@PathVariable Long userId,
                                                             @RequestParam(required = false) Long from,
                                                             @RequestParam(required = false) Long size) {
        return new ResponseEntity<>(eventService.getEvents(List.of(userId),
                        null,
                        null,
                        null,
                        null,
                        from,
                        size).stream().
                map(eventMapper::toEventDto).
                toList(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getFullEvent(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventMapper.toEventDto(event),HttpStatus.OK);
        }
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getPersonalRequests(@PathVariable Long userId) {
        return new ResponseEntity<>(requestService.getRequestsByRequester(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .toList(),HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getPersonalEventRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        return new ResponseEntity<>(requestService.getPersonalEventRequests(userId,eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .toList(),HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<Event> postEvent(
            @Validated @RequestBody NewEventDto newEventDto,
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
            Request request = new Request(null,user,event, event.getRequestModeration() ? "PENDING":"CONFIRMED", LocalDateTime.now());

            return new ResponseEntity<>(RequestMapper.toRequestDto(requestService.createRequest(request)),HttpStatus.CREATED);
        }
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Event> updatePersonalEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Validated @RequestBody EventForUpdate eventForUpdate) {
        Event event = eventService.getEventById(eventId);
        if (eventForUpdate.getStateAction().equals("CANCEL_REVIEW")) {
            event.setState("CANCELED");
        }
        return new ResponseEntity<>(eventService.createEvent(event),HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelEventRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        Request request = requestService.getRequestById(requestId);
        request.setStatus("CANCELED");
        RequestDto requestDto = RequestMapper.toRequestDto(requestService.createRequest(request));
        System.out.println(requestDto);
        return new ResponseEntity<>(requestDto,HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestUserInfo> answerRequests (
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody RequestChangeStatus requestChangeStatus
            ) {
        RequestUserInfo requestUserInfo = requestService.answerRequests(userId,eventId,requestChangeStatus);
        System.out.println(requestUserInfo);
        return new ResponseEntity<>(requestUserInfo,HttpStatus.OK);
    }
}
