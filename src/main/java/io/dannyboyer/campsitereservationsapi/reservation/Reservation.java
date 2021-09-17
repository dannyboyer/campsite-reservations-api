package io.dannyboyer.campsitereservationsapi.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
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
    @NotNull(message = "status must not be null")
    //@Pattern(regexp = "ACTIVE|CANCELED", message = "status must be ACTIVE or CANCELED")
    private ReservationStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id.equals(that.id) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
