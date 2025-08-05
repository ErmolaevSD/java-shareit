package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestClient requestClient;

    @SneakyThrows
    @Test
    void testCreate_whenValid_thenStatusOk() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("description")
                .requestorId(1)
                .created(LocalDateTime.now().plusDays(10))
                .build();

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<ItemRequestDto> argumentCaptor = ArgumentCaptor.forClass(ItemRequestDto.class);
        verify(requestClient).create(eq(1), argumentCaptor.capture());

        ItemRequestDto captorValue = argumentCaptor.getValue();
        assertEquals(itemRequestDto.getDescription(), captorValue.getDescription());
    }

    @SneakyThrows
    @Test
    void testGetMyRequest_whenValid_thenStatusOk() {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(requestClient).getMyRequest(1);
    }

    @SneakyThrows
    @Test
    void testGetRequestById_whenValid_thenStatusOk() {

        mockMvc.perform(get("/requests/{id}", 1))
                .andExpect(status().isOk());

        verify(requestClient).getRequestById(1);
    }
}