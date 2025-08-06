package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private ItemCreatedDto itemCreatedDto;
    private ItemDto itemDto;
    private User user;
    private Booking lastBooking;
    private Booking nextBooking;
    private Item item;
    private ItemRequest itemRequest;
    private List<Item> itemList = new ArrayList<>();
    private CommentDto commentDto;
    private Comment comment;
    private CommentCreatedDto commentCreatedDto;

    @BeforeEach
    void setUp() {
        itemList.add(item);

        user = User.builder()
                .id(1)
                .name("userName")
                .email("user@bk.ru")
                .build();

        lastBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        nextBooking = Booking.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();


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

        comment = Comment.builder()
                .id(1)
                .author(user)
                .text("text")
                .item(item)
                .created(LocalDateTime.now())
                .build();

        commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();


    }

    @Test
    void testCreateItem_whenValid_thenCreatedItem() {
        when(userService.getUser(1))
                .thenReturn(user);
        when(itemMapper.toCreatedDtoItem(itemCreatedDto, requestRepository))
                .thenReturn(item);
        when(requestRepository.findById(1))
                .thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(item))
                .thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto creadtedItemDto = itemService.createItem(1, itemCreatedDto);

        assertNotNull(creadtedItemDto);
        assertEquals(itemCreatedDto.getName(), creadtedItemDto.getName());

        verify(userService).getUser(1);
        verify(itemMapper).toCreatedDtoItem(itemCreatedDto, requestRepository);
        verify(requestRepository).findById(1);
        verify(itemRepository).save(item);
        verify(itemMapper).toItemDto(item);
    }

    @Test
    void testCreateItem_whenNotFoundUser_thenNotFoundException() {
        when(userService.getUser(1))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.createItem(1, itemCreatedDto));
    }

    @Test
    void testGetItem_whenValid_thenReturnedItem() {
        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(1))
                .thenReturn(List.of(new Comment()));
        when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);
        when(bookingRepository.findByItemId(1))
                .thenReturn(List.of(nextBooking, lastBooking));

        ItemDto actualDto = itemService.getItem(1);

        assertNotNull(actualDto);
        assertEquals(nextBooking, actualDto.getNextBooking());
        assertEquals(lastBooking, actualDto.getLastBooking());
        assertEquals(1, actualDto.getComments().size());
    }

    @Test
    void testGetItem_whenNotFoundItem_thenReturnedNotFoundException() {
        when(itemRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItem(1));
    }

    @Test
    void testGetItemByUser_whenValid_thenReturnedItem() {
        when(itemRepository.findAllByOwnerId(1))
                .thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        List<ItemDto> actualItemList = itemService.getItemByUser(1);

        assertNotNull(actualItemList);
        assertEquals(itemDto.getId(), actualItemList.getFirst().getId());
    }

    @Test
    void testSearchItem_whenValid_thenReturnedItem() {
        when(itemRepository.search("text"))
                .thenReturn(List.of(item));

        when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);

        List<ItemDto> actualListItemDto = itemService.searchItem("text");

        assertNotNull(actualListItemDto);
        assertEquals(itemDto, actualListItemDto.getFirst());
    }

    @Test
    void thenSearchItem_whenSearchIsBlank_whenListOf() {
        List<ItemDto> actualListDto = itemService.searchItem("");
        assertEquals(actualListDto, List.of());
    }

    @Test
    void thenPostCommentByItem_whenValid_thenCreatedComment() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndEndIsBefore(eq(1), any(LocalDateTime.class), eq(sort)))
                .thenReturn(List.of(nextBooking));
        when(userService.getUser(1))
                .thenReturn(user);
        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(itemMapper.toCommentCommentDto(comment))
                .thenReturn(commentDto);

        CommentDto actualCommentDto = itemService.postCommentByItem(1, 1, commentCreatedDto);

        assertNotNull(actualCommentDto);
        assertEquals(actualCommentDto.getId(), comment.getId());
    }

    @Test
    void testPostCommentByItem_whenNotFoundItemAndUser() {
        when(itemRepository.findById(1))
                .thenReturn(Optional.empty());

        when(userService.getUser(1))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.postCommentByItem(1, 1, commentCreatedDto));
        assertThrows(NotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void testPostCommentById_whenBookingNotRentItem() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        Booking booking = nextBooking;
        booking.getBooker().setId(100);

        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndEndIsBefore(eq(1), any(LocalDateTime.class), eq(sort)))
                .thenReturn(List.of(booking));

        assertThrows(NotValidException.class, () -> itemService.postCommentByItem(1, 1, commentCreatedDto));
    }


    @Test
    void testUpdateItem_whenValid_thenUpdateOk() {
        when(userService.getUser(1))
                .thenReturn(user);

        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));

        when(itemMapper.toDtoItem(itemDto))
                .thenReturn(item);

        when(itemMapper.updateItemFromDto(itemDto, item))
                .thenReturn(item);

        when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);

        ItemDto actualItemDto = itemService.updateItem(1, 1, itemDto);
        assertNotNull(actualItemDto);
    }

    @Test
    void testUpdate_whenNotOwner_thenNotValidException() {
        when(userService.getUser(1))
                .thenReturn(user);
        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        Item newItem = item;
        newItem.getOwner().setId(199);
        when(itemMapper.toDtoItem(any(ItemDto.class)))
                .thenReturn(newItem);
        when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);

        assertThrows(NotValidException.class, () -> itemService.updateItem(1,1,itemDto));
    }

}