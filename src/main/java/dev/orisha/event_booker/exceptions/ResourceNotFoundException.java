package dev.orisha.event_booker.exceptions;

public class ResourceNotFoundException extends EventBookerBaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
