package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BookingClient bookingClient;



    @Test
    void getBookings() {
    }

    @Test
    void bookItem() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void approveBooking() {
    }

    @Test
    void getAllBookingByOwner() {
    }
}