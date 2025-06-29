package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @RequestBody @Valid ItemDto itemDto) {
        return itemServiceImpl.createItem(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId) {
        return itemServiceImpl.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId,
                          @RequestBody Map<String,Object> updates) {
        return itemServiceImpl.updateItem(ownerId, itemId, updates);
    }
}
