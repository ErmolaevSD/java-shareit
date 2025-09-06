package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserCreatedDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    private User user;
    private UserCreatedDto userCreatedDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("name")
                .email("email@bk.ru")
                .build();

        userCreatedDto = UserCreatedDto.builder()
                .name("name")
                .email("email@bk.ru")
                .build();
    }

    @Test
    void toUser() {
        User actual = userMapper.toUser(userCreatedDto);

        assertEquals(actual.getName(), userCreatedDto.getName());
        assertEquals(actual.getEmail(), userCreatedDto.getEmail());
    }
}