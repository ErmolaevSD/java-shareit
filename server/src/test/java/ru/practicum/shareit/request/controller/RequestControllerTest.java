package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    private ItemRequestCreatedDto itemRequestCreatedDto;
    private RequestResponseDto requestResponseDto;


    @BeforeEach
    void setUp() {
        itemRequestCreatedDto = ItemRequestCreatedDto.builder()
                .description("description")
                .build();

        requestResponseDto = RequestResponseDto.builder()
                .id(1)
                .description("description")
                .requestor(new User())
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }

    @SneakyThrows
    @Test
    void testCreateRequest_whenValid_thenStatusOk() {

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestCreatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(requestService).created(eq(1), any(ItemRequestCreatedDto.class));
    }

    @SneakyThrows
    @Test
    void testGetMyRequest_whenValid_thenStatusOk() {
        when(requestService.getMyRequest(1))
                .thenReturn(List.of(requestResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(1)));

        verify(requestService).getMyRequest(eq(1));
    }

    @SneakyThrows
    @Test
    void testGetRequest_whenValid_thenReturnedRequest() {
        when(requestService.getRequest(any()))
                .thenReturn(requestResponseDto);

        mockMvc.perform(get("/requests/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestResponseDto.getId()))
                .andExpect(jsonPath("$.description").value(requestResponseDto.getDescription()));

        verify(requestService).getRequest(1);
    }

    @SneakyThrows
    @Test
    void testGetAllRequest_whenValid_thenReturnedAllRequest() {
        when(requestService.getOwnerAllRequest(1))
                .thenReturn(List.of(requestResponseDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(1)));

        verify(requestService).getOwnerAllRequest(1);
    }
}