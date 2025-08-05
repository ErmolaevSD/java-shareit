package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    WAITING(""),
    APPROVED("true"),
    REJECTED("false");

    private final String description;

    public static BookingStatus findEnumByDescription(String description) {
        for (BookingStatus bookingStatus : values()) {
            if (bookingStatus.getDescription().equals(description)) {
                return bookingStatus;
            }
        }
        return null;
    }
}