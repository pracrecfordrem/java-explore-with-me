package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.user.User;

public interface UserRepository  extends JpaRepository<User, Long> {
}
