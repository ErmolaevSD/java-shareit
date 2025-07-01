package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemInMemoryRepository {

    private final ItemMapper itemMapper;

    private final Map<Integer, Item> itemMap = new HashMap<>();

    public Item createItem(Integer ownerId, ItemDto itemDto) {
        log.info("Получен запрос на создание вещи {}", itemDto);
        Item item = itemMapper.toDtoItem(itemDto);
        item.setId(getNextId());
        item.setOwnerId(ownerId);
        itemMap.put(item.getId(), item);
        log.info("Успешно создана вещь {}", item);
        return item;
    }

    public List<Item> searchItem(String search) {
        return itemMap.values().stream()
                .filter(item -> (item.getName().equalsIgnoreCase(search) || item.getDescription().equalsIgnoreCase(search)) && item.getAvailable() == true)
                .toList();
    }

    public List<Item> getItemByUser(Integer id) {
        return itemMap.values().stream()
                .filter(item -> item.getOwnerId().equals(id))
                .toList();
    }

    public Item updateItem(Integer itemId,
                           ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи с id {} ", itemId);
        Item item = getItem(itemId);
        itemMapper.updateUserFromDto(itemDto, item);
        return item;
    }

    public Item getItem(Integer id) {
        return itemMap.get(id);
    }

    public void deleteItem(Integer id) {
        itemMap.remove(id);
    }

    public Collection<Item> getAll() {
        return itemMap.values();
    }

    private Integer getNextId() {
        int currentMaxId = itemMap.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}