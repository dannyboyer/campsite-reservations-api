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
    private String id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
}
