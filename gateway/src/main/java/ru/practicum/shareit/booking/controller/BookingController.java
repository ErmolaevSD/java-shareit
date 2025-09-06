package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestCreatedDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get bookings by userID {}", userId);
        return bookingClient.getBookings(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createBookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestBody @Valid BookItemRequestCreatedDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                 @PathVariable @Positive Integer bookingId,
                                                 @RequestParam @NotBlank String approved) {
        log.info("Approved booking {}", bookingId);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                       @RequestParam(required = false) BookingState state) {
        log.info("Get all bookings by OwnerId {}", ownerId);
        return bookingClient.getAllBookingByOwner(Long.valueOf(ownerId), state);
    }
}