package io.dannyboyer.campsitereservationsapi.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Reservation {
    String id;
    @NotEmpty
    String email;
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    LocalDateTime arrivalDate;
    LocalDateTime departureDate;
}
