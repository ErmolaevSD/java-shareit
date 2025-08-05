package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserCreatedDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @SneakyThrows
    @Test
    void testCreateUser_whenValid_thenStatusOk() {

        UserCreatedDto userCreatedDto = UserCreatedDto.builder()
                .email("gan@bk.ru")
                .name("serg")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreatedDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<UserCreatedDto> argumentCaptor = ArgumentCaptor.forClass(UserCreatedDto.class);
        verify(userClient).createUser(argumentCaptor.capture());

        UserCreatedDto captorValue = argumentCaptor.getValue();
        assertEquals(userCreatedDto.getName(), captorValue.getName());
    }

    @SneakyThrows
    @Test
    void testGet_whenValid_thenStatusOk() {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1))
                .andExpect(status().isOk());

        verify(userClient).getUser(1);
    }

    @SneakyThrows
    @Test
    void testDelete_whenValid_thenStatusNoContent() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
                .andExpect(status().isNoContent());

        verify(userClient).deleteUser(1);
    }

    @SneakyThrows
    @Test
    void testUpdate_whenValid_thenStatusOk() {

        Map<String, Object> params = new HashMap<>();
        params.put("email", "gangan@bk.ru");
        params.put("name", "gangan");

        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk());

        verify(userClient).updateUser(1, params);
    }
}