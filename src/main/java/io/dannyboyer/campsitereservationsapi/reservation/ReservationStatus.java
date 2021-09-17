package io.dannyboyer.campsitereservationsapi.reservation;

public enum ReservationStatus {
    ACTIVE("ACTIVE"),
    CANCELED("CANCELED"),
    ;

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
