package com.biblioteka.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage());

        return new ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleConcurrencyConflict(ObjectOptimisticLockingFailureException exception) {
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "The transaction could not be completed because the record was updated by another user. Please try again.");
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

}
