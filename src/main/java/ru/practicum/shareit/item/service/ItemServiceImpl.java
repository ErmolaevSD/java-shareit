package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemInMemoryRepository;

import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl {

    private final ItemInMemoryRepository itemInMemoryRepository;

    public ItemDto createItem(Integer ownerId,
                              ItemDto itemDto) {
        return ItemMapper.toItemDto(itemInMemoryRepository.createItem(ownerId, itemDto));
    }

    public ItemDto getItem(Integer id) {
        if (isNull(itemInMemoryRepository.getItem(id))) {
            throw new NotFoundException("Вещи с id " + id + " не найдено");
        }
        return ItemMapper.toItemDto(itemInMemoryRepository.getItem(id));
    }

    public ItemDto updateItem(Integer ownerId,
                              Integer itemId,
                              Map<String, Object> objects) {
        if (!itemInMemoryRepository.getItem(itemId).getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Обновить вещь может только владелец");
        }
        return ItemMapper.toItemDto(itemInMemoryRepository.updateItem(ownerId, itemId, objects));
    }
}
