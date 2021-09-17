package io.dannyboyer.campsitereservationsapi.reservation;

import io.dannyboyer.campsitereservationsapi.problem.ReservationExceedLimit;
import io.dannyboyer.campsitereservationsapi.problem.ReservationNotFoundException;
import io.dannyboyer.campsitereservationsapi.problem.ReservationTimeConstraint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ReservationService {
    /**
     * Max number of days is 3, but we need to take into consideration
     * that we arrive and leave halfway into the day
     * Therefore, it's really 2 days
     */
    public static final int MAX_NUMBER_OF_DAYS = 2;
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Mono<Reservation> makeReservation(Reservation reservation) {
        var from = reservation.getArrivalDate();
        var to = reservation.getDepartureDate();

        if (from.isAfter(to)) {
            return Mono.error(new ReservationTimeConstraint("arrivalDate cannot be after departureDate"));
        }

        if (Duration.between(from, to).toDays() > MAX_NUMBER_OF_DAYS) {
            return Mono.error(new ReservationExceedLimit());
        }

        // The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance

        // check that campsite is available for timeframe

        return repository.save(reservation);
    }

    public Mono<Reservation> getReservationById(Long id) {
        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new ReservationNotFoundException(id)));
    }

    @Transactional
    public Mono<Reservation> updateReservation(Long id, Reservation updatedReservation) {
        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new ReservationNotFoundException(id)))
                .doOnNext(r -> {
                    r.setArrivalDate(updatedReservation.getArrivalDate());
                    r.setDepartureDate(updatedReservation.getDepartureDate());
                    r.setStatus(updatedReservation.getStatus());
                }).flatMap(repository::save);
    }
}
