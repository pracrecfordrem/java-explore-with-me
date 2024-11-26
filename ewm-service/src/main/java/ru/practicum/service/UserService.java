package ru.practicum.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsers(List<Long> userIds, Integer from, Integer size) {
        List<User> userList = userRepository.getUsers(userIds);
        if (from > userList.size()) {
            return null;
        } else {
            return userList.subList(from, Math.min(from + size,userList.size()));
        }
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
