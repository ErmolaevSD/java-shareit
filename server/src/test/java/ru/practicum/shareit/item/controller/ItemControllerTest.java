package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemCreatedDto itemCreatedDto;
    private ItemDto itemDto;
    private User user;
    private Booking lastBooking;
    private Booking nextBooking;
    private Item item;
    private ItemRequest itemRequest;
    private List<Item> itemList = new ArrayList<>();
    private CommentDto commentDto;
    private CommentCreatedDto commentCreatedDto;

    @BeforeEach
    void setUp() {
        itemList.add(item);

        commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.now())
                .build();

        commentCreatedDto = CommentCreatedDto.builder()
                .text("text")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("itemRequestDescription")
                .requestor(user)
                .items(itemList)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .request(itemRequest)
                .owner(user)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();

        lastBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        user = User.builder()
                .id(1)
                .name("userName")
                .email("user@bk.ru")
                .build();

        itemCreatedDto = ItemCreatedDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(1)
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(1)
                .owner(user)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();

    }


    @SneakyThrows
    @Test
    void testCreate_whenValid_thenStatusOk() {
        when(itemService.createItem(1, itemCreatedDto))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreatedDto)))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<ItemCreatedDto> argumentCaptor = ArgumentCaptor.forClass(ItemCreatedDto.class);
        verify(itemService).createItem(eq(1), argumentCaptor.capture());
        ItemCreatedDto captureValue = argumentCaptor.getValue();

        assertEquals(itemCreatedDto.getName(), captureValue.getName());
    }

    @SneakyThrows
    @Test
    void testGetItem_whenValid_thenReturnedItem() {
        when(itemService.getItem(1))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));

        verify(itemService).getItem(1);
    }

    @SneakyThrows
    @Test
    void testGetAll_whenValid_thenReturnedAllItems() {
        when(itemService.getItemByUser(1))
                .thenReturn(List.of(new ItemDto(), new ItemDto()));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)));
    }

    @SneakyThrows
    @Test
    void testUpdate_whenValid_thenUpdateItem() {
        when(itemService.updateItem(eq(1), eq(1), any(ItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService).updateItem(eq(1), eq(1), any(ItemDto.class));
    }

    @SneakyThrows
    @Test
    void testSearchItem_whenValid_thenReturnedItemList() {
        when(itemService.searchItem("text"))
                .thenReturn(List.of(new ItemDto(), new ItemDto()));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)));

        verify(itemService).searchItem("text");
    }

    @SneakyThrows
    @Test
    void createCommentByItem() {

        when(itemService.postCommentByItem(1, 1, commentCreatedDto))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentCreatedDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<CommentCreatedDto> argumentCaptor = ArgumentCaptor.forClass(CommentCreatedDto.class);
        verify(itemService).postCommentByItem(eq(1), eq(1), argumentCaptor.capture());

        CommentCreatedDto captureValue = argumentCaptor.getValue();
        assertEquals(commentCreatedDto.getText(), captureValue.getText());
    }
}