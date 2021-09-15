package io.dannyboyer.campsitereservationsapi.problem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiError {
    private int errorCode;
    private String message;
    private LocalDateTime timestamp;
}
