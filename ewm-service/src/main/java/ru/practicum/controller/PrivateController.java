package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatClient;
import ru.practicum.model.category.Category;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentMapper;
import ru.practicum.model.comment.CommentDto;
import ru.practicum.model.event.*;
import ru.practicum.model.exception.Exception;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.request.*;
import ru.practicum.model.user.User;
import ru.practicum.repository.LocationRepository;
import ru.practicum.service.*;
import ru.practicum.util.Util;

import java.net.http.HttpRequest;
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
    private final CommentService commentService;
    private final StatClient statClient;

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventDto>> getUserAddedEvents(@PathVariable Long userId,
                                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<EventDto> eventDtoList = eventService.getEvents(List.of(userId),
                        null,
                        null,
                        null,
                        null)
                        .stream()
                        .map(event -> eventMapper.toEventDto(event,statClient))
                        .toList();
        List<EventDto> subEventDtoList = Util.applyPagination(eventDtoList,from, size);
        return new ResponseEntity<>(subEventDtoList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getFullEvent(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventMapper.toEventDto(event,statClient),HttpStatus.OK);
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
    public ResponseEntity<Object> postRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        System.out.println(event.getInitiator().getId() + " " + userId);
        if (event == null || user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else if (requestService.getRequestByRequesterAndEvent(userId,eventId) != null || event.getInitiator().getId().equals(userId) ||
                   !event.getState().equals("PUBLISHED") || (!(event.getParticipantLimit() - eventMapper.toEventDto(event, statClient).getConfirmedRequests() > 0
                || event.getParticipantLimit() == 0))) {
            return new ResponseEntity<>(new Exception("CONFLICT","Integrity constraint has been violated.","could not execute statement; SQL [n/a];" +
                    " constraint [uq_request]; nested exception is org.hibernate.exception.ConstraintViolationException: " +
                    "could not execute statement",LocalDateTime.now()),HttpStatus.CONFLICT);
        } else {
            Request request = new Request(null,user,event,
                    ((event.getParticipantLimit() - eventMapper.toEventDto(event,statClient).getConfirmedRequests() > 0
                            || event.getParticipantLimit() == 0) && !event.getRequestModeration())
                    || event.getParticipantLimit() == 0 ?
                            "CONFIRMED" : "PENDING", LocalDateTime.now());

            return new ResponseEntity<>(RequestMapper.toRequestDto(requestService.createRequest(request)),HttpStatus.CREATED);
        }
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Object> updatePersonalEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Validated @RequestBody EventForUpdate eventForUpdate) {
        Event event = eventService.getEventById(eventId);
        if (event.getState().equals("PUBLISHED")) {
            return new ResponseEntity<>(new Exception("CONFLICT","Integrity constraint has been violated.","could not execute statement; SQL [n/a];" +
                    " constraint [uq_request]; nested exception is org.hibernate.exception.ConstraintViolationException: " +
                    "could not execute statement",LocalDateTime.now()),HttpStatus.CONFLICT);
        } else if (eventForUpdate.getStateAction() != null && eventForUpdate.getStateAction().equals("CANCEL_REVIEW")) {
            event.setState("CANCELED");
        } else if (eventForUpdate.getStateAction() != null && eventForUpdate.getStateAction().equals("SEND_TO_REVIEW")) {
            event.setState("PENDING");
        }
        return new ResponseEntity<>(eventService.createEvent(event),HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<Object> comment(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody CommentDto commentDto) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (user == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "User with id= " + userId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else if (event == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Event with id= " + eventId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else if (!event.getState().equals("PUBLISHED")) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Not authorized to comment not published events", "Event with id= " + eventId + " was not published",LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(commentService.comment(CommentMapper.toComment(user,event, commentDto, "PUBLISHED")),
                HttpStatus.CREATED);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long userId,
                                                @PathVariable Long commentId,
                                                @RequestBody CommentDto commentDto) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Comment with id= " + commentId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else if (!comment.getUser().getId().equals(userId)) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Not authorized to update not own comments", "Comment with id= " + commentId + " was is forbidden to update",LocalDateTime.now()),HttpStatus.CONFLICT);
        } else if (comment.getStatus().equals("BANNED")) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Not authorized to update banned comments", "Comment with id= " + commentId + " is has been banned",LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        comment.setText(commentDto.getText());
        return new ResponseEntity<>(commentService.comment(comment),HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}/complain")
    public ResponseEntity<Object> complain(@PathVariable Long userId,
                                            @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null || !comment.getStatus().equals("PUBLISHED")) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Comment with id= " + commentId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        }
        comment.setStatus("PENDING");
        return new ResponseEntity<>(commentService.comment(comment),HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelEventRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        Request request = requestService.getRequestById(requestId);
        request.setStatus("CANCELED");
        RequestDto requestDto = RequestMapper.toRequestDto(requestService.createRequest(request));
        return new ResponseEntity<>(requestDto,HttpStatus.OK);
    }



    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> answerRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody RequestChangeStatus requestChangeStatus
            ) {
        Event event = eventService.getEventById(eventId);
        if (!(event.getParticipantLimit() - eventMapper.toEventDto(event,statClient).getConfirmedRequests() > 0
                || event.getParticipantLimit() == 0)) {
            return new ResponseEntity<>(new Exception("CONFLICT","Integrity constraint has been violated.","could not execute statement; SQL [n/a];" +
                    " constraint [uq_request]; nested exception is org.hibernate.exception.ConstraintViolationException: " +
                    "could not execute statement",LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        RequestUserInfo requestUserInfo = requestService.answerRequests(userId,eventId,requestChangeStatus);
        return new ResponseEntity<>(requestUserInfo,HttpStatus.OK);
    }
}
