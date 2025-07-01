package ru.practicum.shareit.exception;

public class CreateModelException extends RuntimeException {
    public CreateModelException(String errorMessage) {
        super(errorMessage);
    }
}
