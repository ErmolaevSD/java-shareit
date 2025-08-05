package ru.practicum.shareit.user.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserCreatedDto;

import java.util.Map;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated UserCreatedDto user) {
        return userClient.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable @Positive Integer id) {
        return userClient.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Integer id) {
        userClient.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable @Positive Integer id,
                                         @RequestBody @NotNull Map<String, Object> updates) {
        return userClient.updateUser(id, updates);
    }
}