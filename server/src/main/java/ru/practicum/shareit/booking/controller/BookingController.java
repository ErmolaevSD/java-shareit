package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                     @RequestBody BookingCreatedDto bookingCreatedDto) {
        return bookingService.createBooking(ownerId, bookingCreatedDto);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                         @PathVariable Integer bookingId) {
        return bookingService.getBooking(ownerId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                             @PathVariable Integer bookingId,
                                             @RequestParam String approved) {
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> getAllBookingByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                             @RequestParam(required = false) BookingState state) {
        return bookingService.getAllBookingByUser(ownerId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                              @RequestParam(required = false) BookingState state) {
        return bookingService.getAllBookingByOwner(ownerId, state);
    }
}