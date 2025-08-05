package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemServiceImpl;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @RequestBody ItemCreatedDto itemDto) {
        return itemServiceImpl.createItem(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId) {
        return itemServiceImpl.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemServiceImpl.getItemByUser(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId,
                          @RequestBody ItemDto itemDto) {
        return itemServiceImpl.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(required = false, defaultValue = "") String text) {
        return itemServiceImpl.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createCommentByItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                          @PathVariable Integer itemId,
                                          @RequestBody CommentCreatedDto comment) {
        return itemServiceImpl.postCommentByItem(ownerId, itemId, comment);
    }
}