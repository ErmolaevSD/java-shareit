package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceDb implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public RequestResponseDto created(Integer ownerId, ItemRequestCreatedDto itemRequestCreatedDto) {
        ItemRequest itemRequest = requestMapper.toItemRequest(itemRequestCreatedDto);
        User user = userService.getUser(ownerId);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest save = requestRepository.save(itemRequest);
        return requestMapper.toResponseDto(save);
    }

    @Override
    public List<RequestResponseDto> getMyRequest(Integer ownerId) {
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequestorId(ownerId);
        return itemRequestList.stream().map(requestMapper::toResponseDto).toList();
    }

    @Override
    public List<RequestResponseDto> getOwnerAllRequest(Integer requestId) {
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequestorIdNot(requestId);
        return itemRequestList.stream().map(requestMapper::toResponseDto).toList();
    }

    @Override
    public RequestResponseDto getRequest(Integer id) {
        Optional<ItemRequest> byId = requestRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Запроса с id " + id + " не найдено");
        }

        List<Item> itemList = itemRepository.findAllByRequestId(id);
        byId.get().setItems(itemList);
        RequestResponseDto requestResponseDto = requestMapper.toResponseDto(byId.get());
        return requestResponseDto;
    }
}