package io.ypolin.slideshow.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class SlideshowExceptionHandler {
    record ErrorResponse(String message, String details) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream().map(error ->
                String.format("Field [%s] is invalid: Reason: %s.", error.getField(), error.getDefaultMessage())
        ).collect(Collectors.joining(";"));
        log.error("Request validation error: " + details);
        return new ResponseEntity<>(new ErrorResponse("Request validation error", details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid input", ex);
        return new ResponseEntity<>(new ErrorResponse("Argument is not valid", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAnyException(Exception ex) {
        log.error("Internal error occurred", ex);
        return new ResponseEntity<>(new ErrorResponse("Unexpected error occurred", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
