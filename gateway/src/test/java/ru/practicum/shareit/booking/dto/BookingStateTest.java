package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingStateTest {

    @ParameterizedTest
    @EnumSource(BookingState.class)
    void testAllState_whenValid_thenStatusOk(BookingState bookingState) {

        assertNotNull(bookingState);
        assertEquals(bookingState, BookingState.valueOf(bookingState.name()));
    }

    @Test
    void testAllState_whenValid_thenAllState() {
        List<BookingState> bookingStateList = List.of(BookingState.values());

        assertEquals(6, bookingStateList.size());
    }
}