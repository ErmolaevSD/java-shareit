package ru.practicum.shareit.item.mapper;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {


    @Mock
    private RequestRepository requestRepository;

    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private ItemCreatedDto itemCreatedDto;
    private ItemDto itemDto;
    private User user;
    private Booking lastBooking;
    private Booking nextBooking;
    private Item item;
    private ItemRequest itemRequest;
    private List<Item> itemList = new ArrayList<>();
    private CommentDto commentDto;
    private CommentCreatedDto commentCreatedDto;

    @BeforeEach
    void setUp() {
        itemList.add(item);

        commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.now())
                .build();

        commentCreatedDto = CommentCreatedDto.builder()
                .text("text")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("itemRequestDescription")
                .requestor(user)
                .items(itemList)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .request(itemRequest)
                .owner(user)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();

        lastBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        user = User.builder()
                .id(1)
                .name("userName")
                .email("user@bk.ru")
                .build();

        itemCreatedDto = ItemCreatedDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(1)
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(1)
                .owner(user)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();

    }

    @Test
    void testToCreatedDtoItem_whenValid() {
        Item actualItem = itemMapper.toCreatedDtoItem(itemCreatedDto, requestRepository);

        assertEquals(actualItem.getName(), itemCreatedDto.getName());
        assertEquals(actualItem.getDescription(), itemCreatedDto.getDescription());
        assertEquals(actualItem.getAvailable(), itemCreatedDto.getAvailable());
        assertEquals(actualItem.getNextBooking(), null);
        assertEquals(actualItem.getOwner(), null);
        assertEquals(actualItem.getComments(), null);
    }

    @Test
    void testToItemDto_whenValid() {
        ItemDto actualItemDto = itemMapper.toItemDto(item);

        assertEquals(actualItemDto.getId(), item.getId());
        assertEquals(actualItemDto.getName(), item.getName());
        assertEquals(actualItemDto.getDescription(), item.getDescription());
        assertEquals(actualItemDto.getAvailable(), item.getAvailable());
        assertEquals(actualItemDto.getLastBooking(), item.getLastBooking());
        assertEquals(actualItemDto.getNextBooking(), item.getNextBooking());
        assertEquals(actualItemDto.getOwner(), item.getOwner());
        assertEquals(actualItemDto.getComments(), item.getComments());
    }

    @Test
    void toDtoItem() {
        Item actualItemDto = itemMapper.toDtoItem(itemDto);

        assertEquals(actualItemDto.getId(), itemDto.getId());
        assertEquals(actualItemDto.getName(), itemDto.getName());
        assertEquals(actualItemDto.getDescription(), itemDto.getDescription());
        assertEquals(actualItemDto.getAvailable(), itemDto.getAvailable());
        assertEquals(actualItemDto.getLastBooking(), itemDto.getLastBooking());
        assertEquals(actualItemDto.getNextBooking(), itemDto.getNextBooking());
        assertEquals(actualItemDto.getOwner(), itemDto.getOwner());
        assertEquals(actualItemDto.getComments(), itemDto.getComments());
    }

    @Test
    void toCommentCommentDto() {
        Comment comment = Comment.builder()
                .id(1)
                .item(item)
                .author(user)
                .text("text")
                .created(LocalDateTime.now())
                .build();
        CommentDto actualCommentDto = itemMapper.toCommentCommentDto(comment);

        assertEquals(actualCommentDto.getId(), comment.getId());
        assertEquals(actualCommentDto.getAuthorName(), comment.getAuthor().getName());
        assertEquals(actualCommentDto.getText(), comment.getText());
        assertEquals(actualCommentDto.getCreated(), comment.getCreated());
    }

    @Test
    void mapToRequest() {
        ItemRequest expectedRequest = new ItemRequest();
        when(requestRepository.findById(1))
                .thenReturn(Optional.of(expectedRequest));
        ItemRequest actualRequest = itemMapper.mapToRequest(1, requestRepository);

        assertEquals(expectedRequest, actualRequest);
        verify(requestRepository).findById(1);
    }
}