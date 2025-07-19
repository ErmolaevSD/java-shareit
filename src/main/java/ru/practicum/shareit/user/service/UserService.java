package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CreateModelException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        } else return byId.get();
    }

    public User createUser(User user) {
        if (findUserSameEmail(user.getEmail())) {
            throw new CreateModelException("Пользователь с указанным email уже существует");
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        this.getUser(id);
        userRepository.deleteById(id);
    }

    public User updateUser(Integer id, Map<String, Object> updates) {

        if (findUserSameEmail((String) updates.get("email"))) {
            throw new CreateModelException("Пользователь с указанным email уже существует");
        }
        User user = this.getUser(id);
        user.setEmail((String) updates.get("email"));
        user.setName((String) updates.get("name"));
        return user;
    }

    private boolean findUserSameEmail(String email) {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
        return !users.isEmpty();
    }
}