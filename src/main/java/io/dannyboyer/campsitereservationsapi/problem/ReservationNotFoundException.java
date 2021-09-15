package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super(String.format("Reservation not found for id[%s]", id));
    }
}
