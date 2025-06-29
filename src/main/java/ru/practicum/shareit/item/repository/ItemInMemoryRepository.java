package ru.practicum.shareit.item.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class ItemInMemoryRepository {

    private final Map<Integer, Item> itemMap = new HashMap<>();

    public Item createItem(Integer ownerId, ItemDto itemDto) {
        log.info("Получен запрос на создание вещи {}", itemDto);
        Item item = ItemMapper.toDtoItem(itemDto);
        item.setId(getNextId());
        item.setOwnerId(ownerId);
        itemMap.put(item.getId(), item);
        log.info("Успешно создана вещь {}", item);
        return item;
    }

    public Item updateItem(Integer ownerId,
                           Integer itemId,
                           Map<String, Object> objectMap) {
        log.info("Получен запрос на обновление вещи с id {} ", itemId);
        Item item = getItem(itemId);

        objectMap.forEach((key, value) -> {
            switch (key) {
                case "name":
                    item.setName((String) value);
                    break;
                case "description":
                    item.setDescription((String) value);
                    break;
                case "available":
                    item.setAvailable((Boolean) value);
                    break;
                case "requestId":
                    item.setRequestId((Integer) value);
            }
        });
        return item;
    }

    public Item getItem(Integer id) {
        return itemMap.get(id);
    }

    public void deleteItem(Integer id) {
        itemMap.remove(id);
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
