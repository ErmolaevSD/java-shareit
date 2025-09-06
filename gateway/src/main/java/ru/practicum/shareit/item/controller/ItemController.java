package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @RequestBody @Validated ItemCreatedDto itemDto) {
        return itemClient.create(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable Integer itemId) {
        return itemClient.get(itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                         @PathVariable Integer itemId,
                                         @RequestBody @Validated ItemUpdateDto itemDto) {
        return itemClient.update(ownerId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemClient.getAllItem(ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createCommentByItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                      @PathVariable Integer itemId,
                                                      @RequestBody CommentCreatedDto comment) {
        return itemClient.createComment(ownerId, itemId, comment);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(required = false, defaultValue = "") String text) {
        return itemClient.searchItem(text);
    }
}