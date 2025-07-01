package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    ItemDto createItem(Integer ownerId,
                       ItemDto itemDto);

    ItemDto getItem(Integer id);

    List<ItemDto> getItemByUser(Integer id);

    ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto);

    List<ItemDto> searchItem(String search);
}