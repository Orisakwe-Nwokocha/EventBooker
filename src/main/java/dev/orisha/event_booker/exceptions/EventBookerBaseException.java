package dev.orisha.event_booker.exceptions;

public  abstract class EventBookerBaseException extends RuntimeException {
    public EventBookerBaseException(String message) {
        super(message);
    }
}
