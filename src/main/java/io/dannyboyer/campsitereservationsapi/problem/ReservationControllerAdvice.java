package io.dannyboyer.campsitereservationsapi.problem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ReservationControllerAdvice {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ReservationNotFoundException ex) {
        log.error(ex.getMessage());
        ApiError apiError = ApiError.builder()
                .errorCode(1)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
