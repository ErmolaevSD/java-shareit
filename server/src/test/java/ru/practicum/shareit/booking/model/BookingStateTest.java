package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @ParameterizedTest
    @EnumSource(BookingState.class)
    void findEnumByDescription(BookingState bookingState) {
        assertNotNull(bookingState);
        assertEquals(bookingState, BookingState.findEnumByDescription(bookingState.getDescription()));
    }

    @Test
    void getDescription() {
    }

    @Test
    void testFindByDescription_whenNotValidDescription() {
        BookingState bookingStatus = BookingState.findEnumByDescription("mar");
        assertNull(bookingStatus);
    }

    @Test
    void testGetDescription() {
        BookingState bookingStatus = BookingState.REJECTED;
        String actualStatus = bookingStatus.getDescription();

        assertEquals(bookingStatus.getDescription(), actualStatus);
    }

}