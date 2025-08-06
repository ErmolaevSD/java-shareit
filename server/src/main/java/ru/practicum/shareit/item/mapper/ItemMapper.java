package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "request", source = "requestId", qualifiedByName = "mapToRequest")
    Item toCreatedDtoItem(ItemCreatedDto itemCreatedDto, @Context RequestRepository requestRepository);

    ItemDto toItemDto(Item item);

    Item toDtoItem(ItemDto itemDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentCommentDto(Comment comment);

    @Named("mapToRequest")
    default ItemRequest mapToRequest(Integer requestId, @Context RequestRepository requestRepository) {
        if (requestId == null) {
            return null;
        }
        return requestRepository.findById(requestId).orElse(null);
    }

    @Named("commentToCommentDto")
    default List<CommentDto> mapComment(List<Comment> comments) {
        if (isNull(comments)) {
            return null;
        }
        return comments.stream()
                .map(this::toCommentCommentDto)
                .collect(Collectors.toList());
    }
}