package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(Integer ownerId, BookingCreatedDto bookingCreatedDto);

    BookingResponseDto getBooking(Integer ownerId, Integer bookingId);

    BookingResponseDto approveBooking(Integer ownerId, Integer bookingId, String approved);

    List<Booking> getAllBookingByUser(Integer ownerId, BookingState state);

    List<Booking> getAllBookingByOwner(Integer ownerId, BookingState state);

}
