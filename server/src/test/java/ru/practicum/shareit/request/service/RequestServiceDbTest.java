package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceDbTest {

    @InjectMocks
    private RequestServiceDb requestServiceDb;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    private ItemRequestCreatedDto itemRequestCreatedDto;
    private RequestResponseDto requestResponseDto;
    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("name")
                .email("email@bk.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        itemRequestCreatedDto = ItemRequestCreatedDto.builder()
                .description("description")
                .build();

        requestResponseDto = RequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems())
                .build();
    }

    @Test
    void test_CreatedRequest_whenValid_thenCreatedRequest() {
        when(requestMapper.toItemRequest(itemRequestCreatedDto))
                .thenReturn(itemRequest);

        when(userService.getUser(1))
                .thenReturn(user);

        when(requestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        when(requestMapper.toResponseDto(itemRequest))
                .thenReturn(requestResponseDto);

        RequestResponseDto actualRequest = requestServiceDb.created(1, itemRequestCreatedDto);

        assertNotNull(actualRequest);
        assertEquals(requestResponseDto.getId(), actualRequest.getId());
    }

    @Test
    void testGetMyRequest_whenValid_thenReturnedRequest() {
        when(requestRepository.findAllByRequestorId(1))
                .thenReturn(List.of(itemRequest));
        when(requestMapper.toResponseDto(itemRequest))
                .thenReturn(requestResponseDto);

        List<RequestResponseDto> actualRequestList = requestServiceDb.getMyRequest(1);
        assertNotNull(actualRequestList);
    }

    @Test
    void testGetOwnerAllRequest_whenValid_thenReturnedAllRequests() {

        when(requestRepository.findAllByRequestorIdNot(1))
                .thenReturn(List.of(itemRequest));

        when(requestMapper.toResponseDto(itemRequest))
                .thenReturn(requestResponseDto);

        List<RequestResponseDto> actualRequest = requestServiceDb.getOwnerAllRequest(1);
        assertNotNull(actualRequest);
    }

    @Test
    void testGetRequest_whenValid_thenReturnedRequest() {
        when(requestRepository.findById(1))
                .thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(1))
                .thenReturn(List.of(new Item()));
        when(requestMapper.toResponseDto(itemRequest))
                .thenReturn(requestResponseDto);

        RequestResponseDto actualRequest = requestServiceDb.getRequest(1);

        assertNotNull(actualRequest);
    }

    @Test
    void testGetRequest_whenNotFoundItem_thenNotFoundException() {
        when(requestRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestServiceDb.getRequest(1));
    }
}