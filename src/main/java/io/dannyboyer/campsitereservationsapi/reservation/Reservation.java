package io.dannyboyer.campsitereservationsapi.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    private Long id;
    @NotEmpty(message = "email must not be empty")
    @Email(message = "email must be valid")
    private String email;
    @NotEmpty(message = "firstName must not be empty")
    private String firstName;
    @NotEmpty(message = "lastName must not be empty")
    private String lastName;
    @Future(message = "arrivalDate must be in the future")
    @NotNull(message = "arrivalDate must not be null")
    private LocalDateTime arrivalDate;
    @Future(message = "departureDate must be in the future")
    @NotNull(message = "departurDate must not be null")
    private LocalDateTime departureDate;
}
