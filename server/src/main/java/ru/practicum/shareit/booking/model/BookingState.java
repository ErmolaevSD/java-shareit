package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingState {
    ALL(""),
    WAITING("WAITING"),
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    REJECTED("REJECTED");

    private final String description;

    public static BookingState findEnumByDescription(String description) {
        for (BookingState booking : values()) {
            if (booking.getDescription().equals(description)) {
                return booking;
            }
        }
        return null;
    }
}