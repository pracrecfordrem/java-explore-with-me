package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.model.comment.Comment;
import ru.practicum.repository.CommentRepository;


@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment comment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Page<Comment> getEventComments(Long eventId, Pageable pageable) {
        return commentRepository.getEventComments(eventId, pageable);
    }
}
