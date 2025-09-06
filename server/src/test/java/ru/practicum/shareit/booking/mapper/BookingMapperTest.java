package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    private Booking booking;
    private BookingCreatedDto bookingCreatedDto;

    @BeforeEach
    void setUp() {

        bookingCreatedDto = BookingCreatedDto.builder()
                .itemId(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now()).build();

        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(new User())
                .item(new Item())
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void toBookingResponseDto() {
        BookingResponseDto actual = bookingMapper.toBookingResponseDto(booking);

        assertEquals(actual.getStart(), booking.getStart());
        assertEquals(actual.getEnd(), booking.getEnd());
    }

    @Test
    void toDtoBooking() {

        Booking actual = bookingMapper.toDtoBooking(bookingCreatedDto);

        assertEquals(actual.getStart(), bookingCreatedDto.getStart());
        assertEquals(actual.getEnd(), bookingCreatedDto.getEnd());
    }


    @Test
    void updateBookingFromDto() {
        Booking actual = bookingMapper.updateBookingFromDto(bookingCreatedDto, booking);

        assertEquals(actual.getEnd(), bookingCreatedDto.getEnd());
    }
}