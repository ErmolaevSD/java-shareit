package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable Integer id,
                       @RequestBody Map<String, Object> updates) {
        return userService.updateUser(id, updates);
    }
}