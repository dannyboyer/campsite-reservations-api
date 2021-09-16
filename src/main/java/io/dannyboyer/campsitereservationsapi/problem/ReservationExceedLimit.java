package io.dannyboyer.campsitereservationsapi.problem;

public class ReservationExceedLimit extends RuntimeException{
    public ReservationExceedLimit() {
        super("The campsite can be reserved for max 3 days.");
    }
}
