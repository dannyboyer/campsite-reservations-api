package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationTimeConstraint extends RuntimeException {
    public static final int API_ERROR_CODE = 4;
    public ReservationTimeConstraint(String message) {
        super(message);
    }
}
