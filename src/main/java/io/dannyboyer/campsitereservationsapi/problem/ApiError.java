package io.dannyboyer.campsitereservationsapi.problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int errorCode;
    private String message;
    private List<String> debugMessages;
    private LocalDateTime timestamp;
}
