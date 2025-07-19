package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
@Data
public class UserInMemoryRepository {

    private final Map<Integer, User> userMap = new HashMap<>();

    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя {}", user);
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        return user;
    }

    public void deleteUser(Integer id) {
        log.info("Получен запрос на удаление пользователя с id {} ", id);
        userMap.remove(id);
    }

    public User getUser(Integer id) {
        log.info("Получен запрос на получение пользователя с id {} ", id);
        return userMap.get(id);
    }

    public Collection<User> getAll() {
        return userMap.values();
    }

    public User updateUser(Integer id,
                           Map<String, Object> objectMap) {
        log.info("Получен запрос на обновление пользователя с id {} ", id);
        User user = this.getUser(id);
        objectMap.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
            }
        });
        return user;
    }

    private Integer getNextId() {
        int currentMaxId = userMap.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}