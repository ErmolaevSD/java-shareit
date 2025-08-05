package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.dto.UserCreatedDto;
import ru.practicum.shareit.user.model.User;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserCreatedDto userCreatedDto(User user);

    User toUser(UserCreatedDto userCreatedDto);
}
