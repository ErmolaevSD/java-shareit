package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @MockBean
    private ItemRepository itemRepository;


    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void testSuccessCreate() {

        Integer ownerId = 1;
        User user = new User();
        user.setId(1);

        ItemCreatedDto itemCreatedDto = ItemCreatedDto.builder()
                .name("name")
                .available(true)
                .description("description")
                .requestId(1)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .owner(user)
                .lastBooking(new Booking())
                .nextBooking(new Booking())
                .comments(new ArrayList<>())
                .build();

        when(itemClient.create(ownerId.longValue(), itemCreatedDto))
                .thenReturn(ResponseEntity.ok(itemDto));

        String result = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreatedDto))
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).create(ownerId.longValue(), itemCreatedDto);
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void testSuccessGetById() {

        Integer itemId = 1;
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .owner(new User())
                .lastBooking(new Booking())
                .nextBooking(new Booking())
                .comments(new ArrayList<>())
                .build();

        when(itemClient.get(itemId))
                .thenReturn(ResponseEntity.ok(itemDto));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).get(1);
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void testSuccessUpdate() {

        User user = new User();
        user.setId(1);

        Integer itemId = 1;
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("new_name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .description("description")
                .name("new_name")
                .build();

        when(itemClient.update(1, itemId, itemUpdateDto))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andReturn()
                .getResponse();

        verify(itemClient).update(1, itemId, itemUpdateDto);
    }

    @SneakyThrows
    @Test
    void testSuccessGetAll() {

        User user = new User();
        user.setId(1);

        ItemDto itemDto = ItemDto.builder()
                .owner(user).build();
        ItemDto itemDto1 = ItemDto.builder()
                .owner(user).build();
        ItemDto itemDto2 = ItemDto.builder()
                .owner(user).build();

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        itemDtoList.add(itemDto1);
        itemDtoList.add(itemDto2);

        when(itemClient.getAllItem(1))
                .thenReturn(ResponseEntity.ok(itemDtoList));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).getAllItem(1);
    }

    @SneakyThrows
    @Test
    void testSuccessCreateCommentByItem() {
        Integer ownerId = 1;
        Integer itemId = 2;
        CommentCreatedDto commentCreatedDto = CommentCreatedDto.builder()
                .text("comment").build();

        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("comment")
                .authorName("name")
                .created(LocalDateTime.of(2022, 10, 10, 10, 10))
                .build();

        when(itemClient.createComment(ownerId, itemId, commentCreatedDto))
                .thenReturn(ResponseEntity.ok(commentDto));

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreatedDto))
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).createComment(ownerId, itemId, commentCreatedDto);
        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }

    @SneakyThrows
    @Test
    void testSuccessSearchItem() {

        String text = "description";

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("new_name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        ItemDto itemDto1 = ItemDto.builder()
                .id(2)
                .name("new_name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        itemDtoList.add(itemDto1);

        when(itemClient.searchItem("description"))
                .thenReturn(ResponseEntity.ok(itemDtoList));

        mockMvc.perform(get("/items/search?text={text}", text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).searchItem(text);
    }
}