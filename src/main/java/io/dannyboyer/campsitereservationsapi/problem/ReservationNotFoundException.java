package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationNotFoundException extends RuntimeException {
    public static final int API_ERROR_CODE = 2;
    public ReservationNotFoundException(Long id) {
        super(String.format("Reservation not found for id[%s]", id));
    }
}
