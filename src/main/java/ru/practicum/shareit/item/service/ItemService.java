package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;


public interface ItemService {
    ItemDto createItem(Integer ownerId,
                    ItemCreatedDto itemDto);

    ItemDto getItem(Integer id);

    List<ItemDto> getItemByUser(Integer id);

    ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto);

    List<ItemDto> searchItem(String search);

    CommentDto postCommentByItem(Integer ownerId, Integer itemId, CommentCreatedDto comment);
}