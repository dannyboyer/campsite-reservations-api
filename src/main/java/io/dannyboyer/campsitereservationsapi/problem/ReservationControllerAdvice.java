package io.dannyboyer.campsitereservationsapi.problem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ReservationControllerAdvice {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiError> handleInputValidation(WebExchangeBindException ex) {
        ApiError apiError = new ApiError(0, "Validation failed for reservation inputs", LocalDateTime.now());
        apiError.setDebugMessages(ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList())
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ReservationNotFoundException ex) {
        ApiError apiError = new ApiError(ReservationNotFoundException.API_ERROR_CODE, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationExceedLimit.class)
    public ResponseEntity<ApiError> handleExceedLimit(ReservationExceedLimit ex) {
        ApiError apiError = new ApiError(ReservationExceedLimit.API_ERROR_CODE, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationTimeConstraint.class)
    public ResponseEntity<ApiError> handleArrivalAfterDeparture(ReservationTimeConstraint ex) {
        ApiError apiError = new ApiError(ReservationTimeConstraint.API_ERROR_CODE, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDatabaseIntegrity.class)
    public ResponseEntity<ApiError> handleOverlap(ReservationDatabaseIntegrity ex) {
        ApiError apiError = new ApiError(ReservationDatabaseIntegrity.API_ERROR_CODE, ex.getMessage(), LocalDateTime.now());
        apiError.setDebugMessages(Collections.singletonList(ex.getCause().getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
