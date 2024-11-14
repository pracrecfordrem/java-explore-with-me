package ru.practicum.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository  extends JpaRepository<User, Long> {
    @Query( value = " select new ru.practicum.model.user.User(u.id, u.name, u.email) " +
                                       "   from ru.practicum.model.user.User u" +
            "                             where (:userIds IS NULL or u.id in :userIds)          " )
    List<User> getUsers(@Param("userIds") List<Long> userIds);

    User getUserById(Long userId);
}
