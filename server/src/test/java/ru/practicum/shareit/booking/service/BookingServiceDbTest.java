package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceDbTest {

    @InjectMocks
    private BookingServiceDb bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemMapper itemMapper;

    private Item item;
    private User booker;
    private User user;
    private Booking booking;
    private BookingResponseDto bookingResponseDto;
    private ItemDto itemDto;
    private BookingCreatedDto bookingCreatedDto;

    @BeforeEach
    void setUp() {

        bookingCreatedDto = BookingCreatedDto.builder()
                .itemId(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        user = User.builder()
                .id(1)
                .name("userName")
                .email("user@bk.ru").build();

        item = Item.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .owner(user)
                .lastBooking(new Booking())
                .nextBooking(new Booking())
                .comments(new ArrayList<>())
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .requestId(1)
                .owner(user)
                .lastBooking(new Booking())
                .nextBooking(new Booking())
                .comments(new ArrayList<>())
                .build();

        booker = User.builder()
                .id(1)
                .name("bookerName")
                .email("booker@bk.ru").build();


        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING).build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1)
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void testCreateBooking_whenValid_thenCreatedBooking() {

        when(userService.getUser(anyInt()))
                .thenReturn(user);
        when(itemService.getItem(anyInt()))
                .thenReturn(itemDto);
        when(bookingMapper.toDtoBooking(bookingCreatedDto))
                .thenReturn(booking);
        when(bookingRepository.save(booking))
                .thenReturn(booking);
        when(bookingMapper.toBookingResponseDto(booking))
                .thenReturn(bookingResponseDto);

        BookingResponseDto actualResponseBooking = bookingService.createBooking(1, bookingCreatedDto);
        assertNotNull(actualResponseBooking);
    }

    @Test
    void testGetBooking_whenValid_thenReturnedBooking() {

        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingResponseDto(booking))
                .thenReturn(bookingResponseDto);

        BookingResponseDto actualBooking = bookingService.getBooking(1, 1);

        assertNotNull(actualBooking);
        assertEquals(booking.getId(), actualBooking.getId());
    }

    @Test
    void testGetBooking_whenNotFoundBookingId_thenNotFoundException() {
        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(1, 1));
    }

    @Test
    void testGetBooking_whenNotValidateOwnerId_thenNotValidException() {
        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));

        assertThrows(NotValidException.class, () -> bookingService.getBooking(10, 1));
    }

    @Test
    void testApproveBooking_whenNoFoundBooking_thenNotFountException() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(1, 10, "true"));
    }

    @Test
    void testApproveBooking_whenNotValid_thenNotValidException() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        assertThrows(NotValidException.class, () -> bookingService.approveBooking(10, 1, "true"));
    }


    @Test
    void testApproveBooking_whenValid_thenApprovedBooking() {
        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingResponseDto approvedBooking = bookingResponseDto;
        approvedBooking.setStatus(BookingStatus.APPROVED);

        when(bookingMapper.toBookingResponseDto(any()))
                .thenReturn(approvedBooking);

        BookingResponseDto actualBooking = bookingService.approveBooking(1, 1, "true");

        assertNotNull(actualBooking);
        assertEquals(BookingStatus.APPROVED, actualBooking.getStatus());
        verify(bookingRepository).save(argThat(b -> b.getStatus() == BookingStatus.APPROVED));
    }

    @Test
    void testGetAllBookingByUserALL_whenValid_thenReturnedAllBooking() {
        when(userService.getUser(1))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerId(1, Sort.by(Sort.Direction.DESC, "start")))
                .thenReturn(List.of(new Booking(), new Booking()));

        List<Booking> actualBookingAllList = bookingService.getAllBookingByOwner(1, BookingState.ALL);

        assertNotNull(actualBookingAllList);
        assertEquals(2, actualBookingAllList.size());
    }

    @Test
    void testGetAllBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerId(eq(1), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.ALL);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetCurrentBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(eq(1), any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.CURRENT);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetPastBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndEndIsBefore(eq(1), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.PAST);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetFutureBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndStartIsAfter(eq(1), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.FUTURE);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetWaitingBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndStatus(eq(1), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.WAITING);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetRejectedBookingByUser_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndStatus(eq(1), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByUser(1, BookingState.REJECTED);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }


    @Test
    void testGetAllBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerId(eq(1), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.ALL);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetCurrentBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(eq(1), any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.CURRENT);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetPastBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerIdAndEndIsBefore(eq(1), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.PAST);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetFutureBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerIdAndStartIsAfter(eq(1), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.FUTURE);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetWaitingBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerIdAndStatus(eq(1), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.WAITING);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }

    @Test
    void testGetRejectedBookingByOwner_whenValid_thenReturnedAllBookingByUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        when(bookingRepository.findByItemOwnerIdAndStatus(eq(1), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<Booking> actualListBooking = bookingService.getAllBookingByOwner(1, BookingState.REJECTED);
        assertNotNull(actualListBooking);
        assertEquals(booking, actualListBooking.getFirst());
    }
}