package io.dannyboyer.campsitereservationsapi.problem;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private final int errorCode;
    private final String message;
    private List<String> debugMessages;
    private final LocalDateTime timestamp;
}
