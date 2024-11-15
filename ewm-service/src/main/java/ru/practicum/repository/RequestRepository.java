package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.request.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {
}
