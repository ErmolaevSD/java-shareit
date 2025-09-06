package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestResponseDto create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                     @RequestBody ItemRequestCreatedDto itemDto) {
        return requestService.created(ownerId, itemDto);
    }

    @GetMapping
    public List<RequestResponseDto> getMyRequest(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return requestService.getMyRequest(ownerId);
    }

    @GetMapping("/{id}")
    public RequestResponseDto get(@PathVariable Integer id) {
        return requestService.getRequest(id);
    }

    @GetMapping("/all")
    private List<RequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return requestService.getOwnerAllRequest(ownerId);
    }
}