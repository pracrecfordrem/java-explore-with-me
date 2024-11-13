package ru.practicum.model.user;

public class UserMapper {
    public static User toUser(NewUserDto newUserDto) {
        return new User(null, newUserDto.getName(), newUserDto.getEmail());
    }
}
