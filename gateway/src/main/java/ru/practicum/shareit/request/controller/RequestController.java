package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                         @RequestBody @Validated ItemRequestDto itemRequestCreatedDto) {
        return requestClient.create(ownerId, itemRequestCreatedDto);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequest(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return requestClient.getMyRequest(ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@PathVariable Integer id) {
        return requestClient.getRequestById(id);
    }
}
