package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatClient;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMappper;
import ru.practicum.model.category.NewCategoryDto;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentModeration;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.CompilationForUpdate;
import ru.practicum.model.compilation.NewCompilationDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventDto;
import ru.practicum.model.event.EventForUpdate;
import ru.practicum.model.event.EventMapper;
import ru.practicum.model.exception.Exception;
import ru.practicum.model.user.NewUserDto;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;
import ru.practicum.service.*;
import ru.practicum.util.Util;

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
    private final CompilationService compilationService;
    private final CommentService commentService;

    private final EventMapper eventMapper;
    private final StatClient statClient;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) List<Long> ids,
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
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {
        List<EventDto> eventDtoList = eventService.getEvents(users,
                        states,
                        categories,
                        rangeStart == null ? null : LocalDateTime.parse(rangeStart,CUSTOM_FORMATTER),
                        rangeEnd == null ? null : LocalDateTime.parse(rangeEnd,CUSTOM_FORMATTER)
                       ).stream()
                        .map(event -> eventMapper.toEventDto(event,statClient))
                        .toList();
        List<EventDto> subEventDtoList = Util.applyPagination(eventDtoList,from,size);
        return new ResponseEntity<>(subEventDtoList,HttpStatus.OK);

    }

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        Category category;
        try {
            category = categoryService.createCategory(CategoryMappper.toCategory(newCategoryDto));
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name]; nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement",LocalDateTime.now()),HttpStatus.CONFLICT);
        }
            return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Validated @RequestBody NewUserDto newUserDto) {
        if (userService.findByEmail(newUserDto.getEmail()) != null) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Integrity constraint has been violated.","could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                    "nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                    LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.createUser(UserMapper.toUser(newUserDto)), HttpStatus.CREATED);
    }

    @PostMapping("/compilations")
    public ResponseEntity<Compilation> createCompilation(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto),HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        List<Event> events = eventService.getEventsByCategoryId(catId);
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND","The required object was not found.","Category with id=" + catId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        }
        if (events != null && !events.isEmpty()) {
            return new ResponseEntity<>(new Exception("CONFLICT","For the requested operation the conditions are not met.","The category is not empty",LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Object> updateCategory(@Validated @RequestBody NewCategoryDto newCategoryDto, @PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        try {
            category.setName(newCategoryDto.getName());
            category = categoryService.createCategory(category);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Integrity constraint has been violated.","could not execute statement; SQL [n/a];" +
                    " constraint [uq_category_name]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                    LocalDateTime.now()),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(categoryService.createCategory(category),HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> updateEvent(@Validated @RequestBody EventForUpdate eventForUpdate,
                                             @PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else if (event.getState() != null && (event.getState().equals("PUBLISHED") || event.getState().equals("CANCELED"))) {
            return new ResponseEntity<>(new Exception("CONFLICT", "Integrity constraint has been violated.","could not execute statement; SQL [n/a];" +
                    " constraint [uq_category_name]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                    LocalDateTime.now()),HttpStatus.CONFLICT);
        } else {
            System.out.println(eventForUpdate);
            if (eventForUpdate.getStateAction() != null && eventForUpdate.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState("PUBLISHED");
            }
            if (eventForUpdate.getStateAction() != null && eventForUpdate.getStateAction().equals("REJECT_EVENT")) {
                event.setState("CANCELED");
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
                event.setEventDate(eventForUpdate.getEventDate());
            }
            if (eventForUpdate.getPaid() != null) {
                event.setPaid(eventForUpdate.getPaid());
            }
            return new ResponseEntity<>(eventService.createEvent(event),HttpStatus.OK);
        }
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<Object> updateCompilation(@Validated @RequestBody CompilationForUpdate compilationDto,
                                                         @PathVariable Long compId) {
        Compilation compilation = compilationService.getCompilationById(compId);
        if (compilation == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Compilation with id= " + compId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else {
            NewCompilationDto newCompilationDto = new NewCompilationDto(compilationDto.getEvents() != null ? compilationDto.getEvents() :
                    compilation.getEvents().stream().map(Event::getId).toList(),compilationDto.getPinned() != null ? compilationDto.getPinned() :
                    compilation.getPinned(),
                    compilationDto.getTitle() != null ? compilationDto.getTitle() : compilation.getTitle());
            return new ResponseEntity<>(compilationService.updateCompilation(newCompilationDto,compId),HttpStatus.OK);
        }
    }

    @PatchMapping("/comments/{commentId}/complains/answer")
    public ResponseEntity<Object> approveComplain(@RequestBody CommentModeration commentModeration,
                                                 @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Comment with id= " + commentId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        } else {
            Util.updateComment(commentModeration, comment);
        }
        return new ResponseEntity<>(commentService.comment(comment),HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<Comment>> getEventComments(@PathVariable Long eventId,
                                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        int pageNumber = from / size;
        int remainder = from % size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Comment> comments = Util.applyPagination(commentService.getEventComments(eventId,pageable).stream().toList(),remainder);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Object> getEventComments(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>(new Exception("NOT_FOUND", "The required object was not found.", "Comment with id= " + commentId + " was not found",LocalDateTime.now()),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comment,HttpStatus.OK);
    }

}