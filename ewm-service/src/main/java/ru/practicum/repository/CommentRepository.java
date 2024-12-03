package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(nativeQuery = true,
            value = "select * " +
                    "  from comments " +
                    " where event_id = ?1" +
                    "   and status = 'PUBLISHED'")
    Page<Comment> getEventComments(Long eventId, Pageable pageable);
}
