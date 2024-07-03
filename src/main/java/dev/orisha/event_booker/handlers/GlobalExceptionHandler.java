package dev.orisha.event_booker.handlers;

import dev.orisha.event_booker.dtos.responses.ErrorResponse;
import dev.orisha.event_booker.exceptions.BadRequestException;
import dev.orisha.event_booker.exceptions.ResourceNotFoundException;
import dev.orisha.event_booker.exceptions.UsernameExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException exception,
                                                        HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("IllegalState")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<?> handleUsernameExistsException(UsernameExistsException exception,
                                                        HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("UsernameExists")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                             HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("ResourceNotFound")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleResourceNotFoundException(BadRequestException exception,
                                                             HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("BadRequest")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

}
