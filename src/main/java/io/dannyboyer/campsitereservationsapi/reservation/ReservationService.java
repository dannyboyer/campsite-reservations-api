package io.dannyboyer.campsitereservationsapi.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReservationService {

    @Transactional
    public Mono<Reservation> makeReservation(Reservation reservation) {

        // The campsite can be reserved for max 3 days.

        // The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance

        return Mono.just(reservation);
    }
}
