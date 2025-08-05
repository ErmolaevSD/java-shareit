package ru.practicum.shareit.booking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private BookingCreatedDto bookingCreatedDto;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        bookingCreatedDto = BookingCreatedDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(new Item())
                .booker(new User())
                .status(BookingStatus.WAITING)
                .build();
    }

    @SneakyThrows
    @Test
    void testCreateBooking_whenValid_thenCreatedBooking() {

        when(bookingService.createBooking(1, bookingCreatedDto))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingCreatedDto))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<BookingCreatedDto> argumentCaptor = ArgumentCaptor.forClass(BookingCreatedDto.class);
        verify(bookingService).createBooking(eq(1), argumentCaptor.capture());

        BookingCreatedDto captureValue = argumentCaptor.getValue();
        assertEquals(bookingCreatedDto.getItemId(), captureValue.getItemId());
    }

    @SneakyThrows
    @Test
    void testGetBooking_whenValid_thenFindBooking() {

        when(bookingService.getBooking(1, 1))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));

        verify(bookingService).getBooking(1, 1);
    }

    @SneakyThrows
    @Test
    void testApproveBooking_whenValidTrue_thenApprovedBooking() {

        when(bookingService.createBooking(1, bookingCreatedDto))
                .thenReturn(bookingResponseDto);
        BookingResponseDto createdBooking = bookingService.createBooking(1, bookingCreatedDto);

        assertEquals(BookingStatus.WAITING, createdBooking.getStatus());

        BookingResponseDto approvedBooking = BookingResponseDto.builder()
                .status(BookingStatus.APPROVED).build();

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookingService).approveBooking(eq(1), eq(1), argumentCaptor.capture());
        String captureValue = argumentCaptor.getValue();
        BookingStatus bookingStatus = BookingStatus.findEnumByDescription(captureValue.toString());

        assertEquals(approvedBooking.getStatus(), bookingStatus);
    }

    @SneakyThrows
    @Test
    void testGetAllBookingByUser_whenValid_thenReturnAllBookingByUser() {
        when(bookingService.getAllBookingByUser(anyInt(), any(BookingState.class)))
                .thenReturn(List.of(new Booking(), new Booking(), new Booking()));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(3)));

        verify(bookingService).getAllBookingByUser(1, BookingState.ALL);
    }

    @SneakyThrows
    @Test
    void testGetAllBookingByOwner_whenValid_thenReturnAllBookingByOwner() {
        when(bookingService.getAllBookingByOwner(anyInt(), any(BookingState.class)))
                .thenReturn(List.of(new Booking(), new Booking()));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)));

        verify(bookingService).getAllBookingByOwner(1, BookingState.ALL);
    }
}