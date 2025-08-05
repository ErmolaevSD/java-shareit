package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;

import java.util.List;

public interface RequestService {

    RequestResponseDto created(Integer ownerId, ItemRequestCreatedDto itemRequestCreatedDto);

    List<RequestResponseDto> getMyRequest(Integer requestId);

    List<RequestResponseDto> getOwnerAllRequest(Integer requestId);

    RequestResponseDto getRequest(Integer id);
}
