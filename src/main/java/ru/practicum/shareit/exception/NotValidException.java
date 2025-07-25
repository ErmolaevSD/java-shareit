package ru.practicum.shareit.exception;

public class NotValidException extends RuntimeException {
    public NotValidException(String errorMessage) {
        super(errorMessage);
    }
}
