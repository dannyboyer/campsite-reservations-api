package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationExceedLimit extends RuntimeException{
    public static final int API_ERROR_CODE = 1;
    public ReservationExceedLimit() {
        super("The campsite can be reserved for max 3 days.");
    }
}
