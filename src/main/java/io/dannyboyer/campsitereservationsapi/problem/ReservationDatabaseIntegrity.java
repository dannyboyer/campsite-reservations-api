package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationDatabaseIntegrity extends RuntimeException{
    public static final int API_ERROR_CODE = 3;
    public ReservationDatabaseIntegrity(Throwable e) {
        super("Reservation database integrity compromised", e);
    }
}
