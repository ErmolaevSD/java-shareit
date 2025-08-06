package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestCreatedDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @SneakyThrows
    @Test
    void testGetAllBooking_whenValid_thenStatusOk() {

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(bookingClient).getBookings(1L);
    }


    @SneakyThrows
    @Test
    void testGetBookingById_whenValid_thenStatusOk() {

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBooking(1, 1L);
    }

    @SneakyThrows
    @Test
    void testCreateBookItem_whenValid_thenStatusOk() {
        BookItemRequestCreatedDto bookingCreatedDto = BookItemRequestCreatedDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingCreatedDto))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        ArgumentCaptor<BookItemRequestCreatedDto> argumentCaptor = ArgumentCaptor.forClass(BookItemRequestCreatedDto.class);
        verify(bookingClient).bookItem(eq(1L), argumentCaptor.capture());

        BookItemRequestCreatedDto captureValue = argumentCaptor.getValue();
        assertEquals(bookingCreatedDto.getItemId(), captureValue.getItemId());
    }

    @SneakyThrows
    @Test
    void testApprovedBooking_whenValid_thenStatusOk() {

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient).approveBooking(1, 1, "true");
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner() {

        BookingState state = BookingState.ALL;

        mockMvc.perform(get("/bookings/owner?state={state}", state)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(bookingClient).getAllBookingByOwner(1L, state);
    }
}