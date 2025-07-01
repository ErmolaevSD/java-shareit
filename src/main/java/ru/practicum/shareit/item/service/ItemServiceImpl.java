package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemInMemoryRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemInMemoryRepository itemInMemoryRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(Integer ownerId,
                              ItemDto itemDto) {
        userService.getUser(ownerId);
        return itemMapper.toItemDto(itemInMemoryRepository.createItem(ownerId, itemDto));
    }

    @Override
    public ItemDto getItem(Integer id) {
        if (isNull(itemInMemoryRepository.getItem(id))) {
            throw new NotFoundException("Вещи с id " + id + " не найдено");
        }
        return itemMapper.toItemDto(itemInMemoryRepository.getItem(id));
    }

    @Override
    public List<ItemDto> getItemByUser(Integer id) {
        return itemInMemoryRepository.getItemByUser(id).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto updateItem(Integer ownerId,
                              Integer itemId,
                              ItemDto itemDto) {
        userService.getUser(ownerId);
        if (!itemInMemoryRepository.getItem(itemId).getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Обновить вещь может только владелец");
        }
        return itemMapper.toItemDto(itemInMemoryRepository.updateItem(itemId, itemDto));
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        if (search.isBlank()) {
            return new ArrayList<>();
        }
        return itemInMemoryRepository.searchItem(search).stream().map(itemMapper::toItemDto).toList();
    }
}