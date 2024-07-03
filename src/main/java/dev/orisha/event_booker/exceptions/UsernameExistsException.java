package dev.orisha.event_booker.exceptions;

public class UsernameExistsException extends EventBookerBaseException {
    public UsernameExistsException(String message) {
        super(message);
    }
}
