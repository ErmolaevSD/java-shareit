package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserCreatedDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreatedDto userCreatedDto;
    private User user;

    @BeforeEach
    void setUp() {

        userCreatedDto = UserCreatedDto.builder()
                .name("name")
                .email("email@bk.ru")
                .build();

        user = User.builder()
                .id(1)
                .name(userCreatedDto.getName())
                .email(userCreatedDto.getEmail()).build();

    }

    @SneakyThrows
    @Test
    void testCreateUser_whenValid_thenStatusOk() {
        when(userService.createUser(userCreatedDto))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCreatedDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<UserCreatedDto> argumentCaptor = ArgumentCaptor.forClass(UserCreatedDto.class);
        verify(userService).createUser(argumentCaptor.capture());

        UserCreatedDto captureValue = argumentCaptor.getValue();
        assertEquals(userCreatedDto.getName(), captureValue.getName());
    }

    @SneakyThrows
    @Test
    void testGetUser_whenValid_thenReturnedUser() {
        when(userService.getUser(1))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService).getUser(1);
    }

    @SneakyThrows
    @Test
    void testDeleteUser_whenValid_thenUserDeleted() {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1);
    }

    @SneakyThrows
    @Test
    void testUpdateUser_whenValid_thenUpdateUser() {
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("name", "newName");
        stringObjectMap.put("email", "newEmail@bk.ru");

        User updateUser = user;
        updateUser.setName((String) stringObjectMap.get("name"));
        updateUser.setEmail((String) stringObjectMap.get("email"));

        when(userService.updateUser(1, stringObjectMap))
                .thenReturn(updateUser);

        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stringObjectMap)))
                .andExpect(status().isOk());

        verify(userService).updateUser(1, stringObjectMap);
    }
}