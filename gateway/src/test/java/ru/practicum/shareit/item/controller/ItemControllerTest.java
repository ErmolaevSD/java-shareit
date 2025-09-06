package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @SneakyThrows
    @Test
    void testSuccessCreate() {

        Integer ownerId = 1;
        ItemCreatedDto itemCreatedDto = ItemCreatedDto.builder()
                .name("name")
                .available(true)
                .description("description")
                .requestId(1)
                .build();

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreatedDto))
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient).create(ownerId.longValue(), itemCreatedDto);
    }

    @SneakyThrows
    @Test
    void testSuccessGetById() {

        Integer itemId = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).get(1);
    }

    @SneakyThrows
    @Test
    void testSuccessUpdate() {

        Integer itemId = 1;
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .description("description")
                .name("new_name")
                .build();

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).update(eq(1), eq(1), any(ItemUpdateDto.class));
    }

    @SneakyThrows
    @Test
    void testSuccessGetAll() {

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
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

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreatedDto))
                        .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient).createComment(ownerId, itemId, commentCreatedDto);
    }

    @SneakyThrows
    @Test
    void testSuccessSearchItem() {

        String text = "description";

        mockMvc.perform(get("/items/search?text={text}", text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemClient).searchItem(text);
    }
}