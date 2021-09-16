package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationTimeConstraint extends RuntimeException {
    public ReservationTimeConstraint(String message) {
        super(message);
    }
}
