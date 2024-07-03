package dev.orisha.event_booker.exceptions;

public class BadRequestException extends EventBookerBaseException {
    public BadRequestException(String message) {
        super(message);
    }
}
