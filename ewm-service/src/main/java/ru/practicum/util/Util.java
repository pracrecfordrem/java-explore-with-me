package ru.practicum.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentModeration;
import ru.practicum.model.exception.Exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Util {
    public static <T> List<T> applyPagination(List<T> list, int from, int size) {
        if (from >= list.size()) return Collections.emptyList();
        return list.subList(from, Math.min(from + size, list.size()));
    }

    public static <T> List<T> applyPagination(List<T> list, int from) {
        if (from >= list.size()) return Collections.emptyList();
        return list.subList(from, list.size());
    }

    public static Comment updateComment(CommentModeration commentModeration, Comment comment) {
        if (commentModeration.getAction().equals("APPROVE_COMPLAIN")) {
            comment.setStatus("BANNED");
        } else if (commentModeration.getAction().equals("DISPROVE_COMPLAIN")) {
            comment.setStatus("PUBLISHED");
        }
        return comment;
    }
}
