package ru.yandex.practicum.filmorate.exception;

public class NotSupportedOperationException extends RuntimeException {
    public NotSupportedOperationException(String message) {
        super(message);
    }
}
