package io.ypolin.slideshow.api;

import io.ypolin.slideshow.service.exception.ResourceNotFoundException;
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

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        String details = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException validationException) {
            details = validationException.getBindingResult().getFieldErrors().stream().map(error ->
                    String.format("Field [%s] is invalid: Reason: %s.", error.getField(), error.getDefaultMessage())
            ).collect(Collectors.joining(";"));
        }
        log.error("Request validation exception: " + details);
        return new ResponseEntity<>(new ErrorResponse("Request validation error", details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found exception", ex);
        return new ResponseEntity<>(new ErrorResponse("Resource not found error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAnyException(Exception ex) {
        log.error("Internal error occurred", ex);
        return new ResponseEntity<>(new ErrorResponse("Unexpected error occurred", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
