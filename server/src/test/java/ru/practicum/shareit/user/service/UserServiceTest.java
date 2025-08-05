package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.CreateModelException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreatedDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserCreatedDto userCreatedDto;
    private User user;
    private User user2;

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

        user2 = User.builder()
                .id(2)
                .name("name2")
                .email("nem@bk.ru").build();
    }

    @Test
    void testCreateUser_whenValid_thenCreatedUser() {
        when(userRepository.findAll())
                .thenReturn(List.of(user2));
        when(userMapper.toUser(userCreatedDto))
                .thenReturn(user2);

        userService.createUser(userCreatedDto);
        verify(userRepository).save(user2);
    }

    @Test
    void testCreateUser_whenNotValid_thenCreateModelException() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        assertThrows(CreateModelException.class, () -> userService.createUser(userCreatedDto));
        verify(userMapper, never()).toUser(userCreatedDto);
        verify(userRepository, never()).save(user);
    }

    @Test
    void testGetUser_whenValid_thenReturnedUser() {
        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));
        User actualUser = userService.getUser(1);

        assertEquals(user, actualUser);
    }

    @Test
    void testGetUser_whenNotFoundUser_thenNotFoundException() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void testDeleteUser_whenValid_thenDeletedUser() {
        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteUser_whenNotFoundUser_thenNotFoundException() {
        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1));

        verify(userRepository, never()).deleteById(1);
    }

    @Test
    void testUpdateUser_whenValid_thenUpdateUser() {

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "newName");
        updates.put("email", "new@bk.ru");

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        User actualUser = userService.updateUser(1, updates);

        assertEquals(updates.get("name"), actualUser.getName());
        assertEquals(updates.get("email"), actualUser.getEmail());
    }

    @Test
    void testUpdateUser_whenNotValid_thenCreateModelException() {

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "newName");
        updates.put("email", "email@bk.ru");

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        assertThrows(CreateModelException.class, () -> userService.updateUser(1, updates));
    }
}