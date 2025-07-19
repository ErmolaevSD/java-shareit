package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CreateModelException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserInMemoryRepository;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInMemoryRepository userInMemoryRepository;

    public User getUser(Integer id) {
        if (isNull(userInMemoryRepository.getUser(id))) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        return userInMemoryRepository.getUser(id);
    }

    public User createUser(User user) {
        if (findUserSameEmail(user.getEmail())) {
            throw new CreateModelException("Пользователь с указанным email уже существует");
        }
        if (isNull(userInMemoryRepository.getUser(user.getId()))) {
            return userInMemoryRepository.createUser(user);
        }
        throw new CreateModelException("Пользователь с id: " + user.getId() + " уже существует");
    }

    public void deleteUser(Integer id) {
        if (isNull(userInMemoryRepository.getUser(id))) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        userInMemoryRepository.deleteUser(id);
    }

    public User updateUser(Integer id, Map<String, Object> updates) {

        if (findUserSameEmail((String) updates.get("email"))) {
            throw new CreateModelException("Пользователь с указанным email уже существует");
        }

        if (isNull(userInMemoryRepository.getUser(id))) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        return userInMemoryRepository.updateUser(id, updates);
    }

    private boolean findUserSameEmail(String email) {
        List<User> users = userInMemoryRepository.getAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
        return !users.isEmpty();
    }
}