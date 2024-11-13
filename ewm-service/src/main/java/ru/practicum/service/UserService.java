package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
