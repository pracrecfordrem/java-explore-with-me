package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CommentMapper {

    public static Comment toComment(User user, Event event, CommentDto commentDto, String status) {
        return new Comment(null,
                user,
                event,
                commentDto.getText(),
                LocalDateTime.now(),
                null,
                status
                );
    }
}
