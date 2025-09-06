package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BookingStatusTest {

    @ParameterizedTest
    @EnumSource(BookingStatus.class)
    void testFindByDescription(BookingStatus bookingState) {

        assertNotNull(bookingState);
        assertEquals(bookingState, BookingStatus.findEnumByDescription(bookingState.getDescription()));
    }

    @Test
    void testFindByDescription_whenNotValidDescription() {
        BookingStatus bookingStatus = BookingStatus.findEnumByDescription("mar");
        assertNull(bookingStatus);
    }

    @Test
    void testValues_whenValid_thenGetAllState() {
        List<BookingStatus> bookingStateList = List.of(BookingStatus.values());

        assertEquals(3, bookingStateList.size());
    }

    @Test
    void testGetDescription() {
        BookingStatus bookingStatus = BookingStatus.REJECTED;
        String actualStatus = bookingStatus.getDescription();

        assertEquals(bookingStatus.getDescription(), actualStatus);
    }
}