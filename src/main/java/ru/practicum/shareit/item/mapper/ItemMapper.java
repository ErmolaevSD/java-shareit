package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    Item toCreatedDtoItem(ItemCreatedDto itemCreatedDto);

    ItemDto toItemDto(Item item);

    Item toDtoItem(ItemDto itemDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentCommentDto(Comment comment);


    default List<CommentDto> mapComment(List<Comment> comments) {
        if (isNull(comments)) {
            return null;
        }
        return comments.stream()
                .map(this::toCommentCommentDto)
                .collect(Collectors.toList());
    }
}