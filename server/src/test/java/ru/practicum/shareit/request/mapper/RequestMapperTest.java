package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    private RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    private ItemRequestCreatedDto itemRequestCreatedDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequestCreatedDto = ItemRequestCreatedDto.builder()
                .description("name").build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .items(new ArrayList<>())
                .created(LocalDateTime.now())
                .requestor(new User())
                .description("name")
                .build();
    }


    @Test
    void toItemRequest() {

        ItemRequest actual = requestMapper.toItemRequest(itemRequestCreatedDto);
        ItemRequest requestNull = requestMapper.toItemRequest(null);

        assertEquals(actual.getDescription(), itemRequestCreatedDto.getDescription());
        assertNull(requestNull);
    }

    @Test
    void toResponseDto() {

        RequestResponseDto actual = requestMapper.toResponseDto(itemRequest);
        RequestResponseDto actualNull = requestMapper.toResponseDto(null);

        assertEquals(actual.getDescription(), itemRequest.getDescription());
        assertNull(actualNull);
    }
}